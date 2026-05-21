package com.example.springboot.config;

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
}
