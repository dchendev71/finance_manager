package com.example.springboot.helper;

import com.example.springboot.auth.dto.AuthResponse;
import com.example.springboot.config.TestConfig;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.user.dto.UserResponse;

public class ResponseTestFactory {
  public static class User {
    public static UserResponse create() {
      return UserResponse.builder()
          .email(TestConfig.User.email)
          .currency(EntityTestFactory.CurrencyFactory.createDto())
          .build();
    }
  }

  public static class Auth {
    public static AuthResponse create() {
      return new AuthResponse(TestConfig.Auth.jwt);
    }
  }

  public static class Portfolio {
    public static PortfolioResponse create() {
      return PortfolioResponse.builder()
          .portfolioName(TestConfig.Portfolio.name)
          .userResponse(ResponseTestFactory.User.create())
          .build();
    }
  }
}
