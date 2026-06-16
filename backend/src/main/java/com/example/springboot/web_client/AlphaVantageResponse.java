package com.example.springboot.web_client;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AlphaVantageResponse {

  @JsonProperty("Global Quote")
  private GlobalQuote globalQuote;

  public GlobalQuote getGlobalQuote() {
    return globalQuote;
  }

  public static class GlobalQuote {

    @JsonProperty("05. price")
    private double price;

    public double getPrice() {
      return price;
    }
  }
}
