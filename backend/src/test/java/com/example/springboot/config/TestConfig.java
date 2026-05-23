package com.example.springboot.config;

import java.math.BigDecimal;

// This file describes every test constant that will be used

public interface TestConfig {

  interface User {
    String email = "testuser@gmail.com";
    String password = "testpassword";
  }

  interface Currency {
    String code = "USD";
    String name = "US Dollar";
    String symbol = "$";
  }

  interface Auth {
    String jwt =
        "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb2huMTI0QGdtYWlsLmNvbSIsImlhdCI6MTc3OTAxNjI5OCwiZXhwIjoxNzc5MDE5ODk4fQ.4qjsfdmWnNeiFRNISkVsl75SvTDAxrCEdApgt_IW9JMiM2bo02knRTpuODAq0f6L";
  }

  interface Portfolio {
    String name = "testPortfolio";
  }

  interface Asset {
    String name = "NVIDIA";
    String tickerSymbol = "NVDA";
  }

  interface AssetType {
    String type = "stock";
  }

  interface PortfolioAsset {
    BigDecimal quantity = new BigDecimal(10.5);
    BigDecimal price = new BigDecimal(10);
  }
}
