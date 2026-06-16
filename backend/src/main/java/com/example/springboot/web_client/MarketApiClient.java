package com.example.springboot.web_client;

import com.example.springboot.common.exception.NotFoundException;
import com.example.springboot.common.exception.RateLimitException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MarketApiClient {

  private final WebClient webClient;

  private static final String BASE_URL = "https://www.alphavantage.co";

  @Value("${alphavantage.api.key}")
  private String apiKey;

  public MarketApiClient(WebClient.Builder builder) {
    this.webClient = builder.baseUrl(BASE_URL).build();
  }

  public double fetchPrice(String symbol) {
    ObjectMapper mapper = new ObjectMapper();

    return webClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path("/query")
                    .queryParam("function", "GLOBAL_QUOTE")
                    .queryParam("symbol", symbol)
                    .queryParam("apikey", this.apiKey)
                    .build())
        .retrieve()
        .bodyToMono(String.class)
        .map(
            raw -> {
              try {
                JsonNode root = mapper.readTree(raw);

                if (root.has("Information")) {
                  throw new RateLimitException(
                      "API rate limit reached: " + root.get("Information").asText());
                }

                AlphaVantageResponse response =
                    mapper.treeToValue(root, AlphaVantageResponse.class);

                if (response == null || response.getGlobalQuote() == null) {
                  throw new NotFoundException(MarketApiClient.class, "Symbol not found: " + symbol);
                }
                if (response.getGlobalQuote().getPrice() == 0.0) {
                  throw new NotFoundException(
                      MarketApiClient.class, "No price data for symbol: " + symbol);
                }
                return response.getGlobalQuote().getPrice();

              } catch (JsonProcessingException e) {
                throw new NotFoundException(
                    MarketApiClient.class, "Failed to parse response for: " + symbol);
              }
            })
        .block();
  }
}
