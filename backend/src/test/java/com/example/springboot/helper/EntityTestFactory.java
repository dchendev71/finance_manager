package com.example.springboot.helper;

import com.example.springboot.config.TestConfig;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.dto.CurrencyDto;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.user.User;

public class EntityTestFactory {
  public static class CurrencyFactory {
    public static Currency create() {
      return Currency.builder()
          .code(TestConfig.Currency.code)
          .name(TestConfig.Currency.name)
          .symbol(TestConfig.Currency.symbol)
          .build();
    }

    public static CurrencyDto createDto() {
      return CurrencyDto.builder()
          .code(TestConfig.Currency.code)
          .name(TestConfig.Currency.name)
          .symbol(TestConfig.Currency.symbol)
          .build();
    }
  }

  public static class UserFactory {
    public static User create() {
      return User.builder()
          .id(1L)
          .email(TestConfig.User.email)
          .password(TestConfig.User.password)
          .active(true)
          .currency(EntityTestFactory.CurrencyFactory.create())
          .build();
    }

    public static User create(String email) {
      User user = EntityTestFactory.UserFactory.create();

      user.setEmail(email);
      return user;
    }
  }

  public static class PortfolioFactory {
    public static Portfolio create() {
      return Portfolio.builder()
          .name(TestConfig.Portfolio.name)
          .user(EntityTestFactory.UserFactory.create())
          .build();
    }
  }
}
