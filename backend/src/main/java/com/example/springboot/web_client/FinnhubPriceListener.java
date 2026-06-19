package com.example.springboot.web_client;

import com.example.springboot.web_socket.PriceWebSocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class FinnhubPriceListener {

  private final RedisTemplate<String, Double> redisTemplate;
  private final PriceWebSocketHandler priceWebSocketHandler;

  @Value("${finnhub.api.key}")
  private String apiKey;

  private static final String PRICE_KEY_PREFIX = "price:";
  private static final int MAX_RETRIES = 10;

  private int retryCount = 0;

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
          }

          @Override
          public void onMessage(String message) {
            handlePriceUpdate(message);
          }

          @Override
          public void onClose(int code, String reason, boolean remote) {
            if (retryCount >= MAX_RETRIES) {
              System.out.println("Max reconnect attempts reached. Giving up.");
              return;
            }

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

          redisTemplate.opsForValue().set(PRICE_KEY_PREFIX + symbol, price, Duration.ofMinutes(5));
          // Push the information to subscribed ws
        }
      }
    } catch (Exception ex) {
      System.out.println("Failed to parse price update: " + ex.getMessage());
    }
  }
}
