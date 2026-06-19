package com.example.springboot.web_client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class FinnhubPriceListener {

  private final RedisTemplate<String, Double> redisTemplate;

  @Value("${finnhub.api.key}")
  private String apiKey;

  private static final String PRICE_KEY_PREFIX = "price:";
  private static final int MAX_RETRIES = 10;
  private static final String FINNHUB_QUOTE_URL = "https://finnhub.io/api/v1/quote";

  private int retryCount = 0;

  private void fetchInitialPrice(String symbol) {
    try {
      JsonNode response =
          RestClient.create()
              .get()
              .uri(FINNHUB_QUOTE_URL + "?symbol=" + symbol + "&token=" + apiKey)
              .retrieve()
              .body(JsonNode.class);

      double price = response.get("c").asDouble();
      redisTemplate.opsForValue().set(PRICE_KEY_PREFIX + symbol, price);
      log.info("Initial price for {}: {}", symbol, price);
    } catch (Exception e) {
      log.error("Failed to fetch initial price for {}", symbol, e);
    }
  }

  public void connect(List<String> symbols) {
    URI uri = URI.create("wss://ws.finnhub.io?token=" + apiKey);

    WebSocketClient client =
        new WebSocketClient(uri) {

          @Override
          public void onOpen(ServerHandshake handshake) {
            System.out.println("WebSocket connected");
            retryCount = 0;
            symbols.forEach(
                symbol -> send("{\"type\":\"subscribe\",\"symbol\":\"" + symbol + "\"}"));

            CompletableFuture.runAsync(() -> symbols.forEach(symbol -> fetchInitialPrice(symbol)));
          }

          @Override
          public void onMessage(String message) {
            if (message.contains("\"type\":\"ping\"")) return; // ignore ping
            handlePriceUpdate(message);
          }

          @Override
          public void onClose(int code, String reason, boolean remote) {
            if (retryCount >= MAX_RETRIES) {
              System.out.println("Max reconnect attempts reached. Giving up.");
              return;
            }
            // Delete redis cache as we populate the data only from this entry
            symbols.forEach(symbol -> redisTemplate.delete(PRICE_KEY_PREFIX + symbol));

            retryCount++;
            long delay =
                Math.min(5000L * retryCount, 60_000L); // exponential backoff, capped at 60s

            System.out.println(
                "WebSocket closed (attempt "
                    + retryCount
                    + "), reconnecting in "
                    + delay
                    + "ms...");

            new Timer()
                .schedule(
                    new TimerTask() {
                      public void run() {
                        FinnhubPriceListener.this.connect(symbols);
                      }
                    },
                    delay);
          }

          @Override
          public void onError(Exception ex) {
            System.out.println("WebSocket error: " + ex.getMessage());
          }
        };

    client.connect();
  }

  private void handlePriceUpdate(String json) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(json);

      if (root.has("data")) {
        for (JsonNode trade : root.get("data")) {
          String symbol = trade.get("s").asText();
          double price = trade.get("p").asDouble();

          if (symbol.startsWith("BINANCE:")) {
            // BINANCE:XXXXUSDT
            symbol = symbol.substring(8, symbol.length() - 4);
          }
          redisTemplate.opsForValue().set(PRICE_KEY_PREFIX + symbol, price);
          // Push the information to subscribed ws
        }
      }
    } catch (Exception ex) {
      System.out.println("Failed to parse price update: " + ex.getMessage());
    }
  }
}
