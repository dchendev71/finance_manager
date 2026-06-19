package com.example.springboot.web_socket;

import com.example.springboot.asset.Asset;
import com.example.springboot.asset.AssetService;
import com.example.springboot.price.PriceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("!test")
public class PriceWebSocketHandler extends TextWebSocketHandler {
  private final AssetService assetService;
  private final ObjectMapper objectMapper;
  private final PriceService priceService;

  private final Map<String, Set<WebSocketSession>> subscribers = new ConcurrentHashMap<>();

  @PostConstruct
  public void init() {
    List<Asset> assets = assetService.getAssets();
    for (Asset asset : assets) {
      String tickerSymbol = asset.getTickerSymbol();
      subscribers.put(tickerSymbol, ConcurrentHashMap.newKeySet());
    }
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) {
    // TODO: Fix with logger:?
    System.out.println("Received connection from ws");
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    subscribers.values().forEach(sessions -> sessions.remove(session));
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage message) {
    try {
      JsonNode payload = objectMapper.readTree(message.getPayload());
      String action = payload.get("action").asText();
      List<String> symbols = objectMapper.convertValue(payload.get("symbols"), List.class);

      if ("subscribe".equals(action)) {
        symbols.forEach(
            symbol ->
                subscribers
                    .computeIfAbsent(symbol, k -> ConcurrentHashMap.newKeySet())
                    .add(session));
      }
      System.out.println("Received TextMessage with following symbols" + symbols);
    } catch (JsonProcessingException e) {
      log.error(
          "Invalid message format from session {}: {}", session.getId(), message.getPayload(), e);
      try {
        session.sendMessage(new TextMessage("{\"error\":\"Invalid message format\"}"));
      } catch (IOException ex) {
        log.error("Failed to send error message to session {}", session.getId(), ex);
      }
    }
  }

  @Scheduled(fixedRate = 1000) // 1sec
  public void broadcastPrices() throws Exception {
    List<String> symbols = new ArrayList<>(subscribers.keySet());

    symbols.forEach(
        symbol -> {
          double price = priceService.getIndicativePrice(symbol);
          try {
            broadcastPrice(symbol, price);
          } catch (Exception e) {
            log.error("Failed to broadcast symbol {}", symbol);
          }
        });
  }

  public void broadcastPrice(String symbol, double price) throws Exception {
    String message = "{\"symbol\":\"" + symbol + "\",\"price\":" + price + "}";
    subscribers
        .getOrDefault(symbol, Set.of())
        .forEach(
            session -> {
              if (session.isOpen()) {
                try {
                  session.sendMessage(new TextMessage(message));
                } catch (Exception e) {
                  // TODO: Fix with logger
                  System.out.println("Failed to send message to session: " + e);
                }
              }
            });
  }
}
