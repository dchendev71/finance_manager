package com.example.springboot.helper;

import com.example.springboot.config.TestConfig;
import com.example.springboot.currency.Currency;
import com.example.springboot.currency.dto.CurrencyDto;
import com.example.springboot.portfolio.Portfolio;
import com.example.springboot.portfolio.asset.Asset;
import com.example.springboot.portfolio.asset_type.AssetType;
import com.example.springboot.portfolio.portfolio_asset.PortfolioAsset;
import com.example.springboot.user.User;
import java.math.BigDecimal;

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

    public static Portfolio create(String portfolioName) {
      return Portfolio.builder()
          .name(portfolioName)
          .user(EntityTestFactory.UserFactory.create())
          .build();
    }
  }

  public static class AssetTypeFactory {
    public static AssetType create() {
      return AssetType.builder().type(TestConfig.AssetType.type).build();
    }
  }

  public static class AssetFactory {
    public static Asset create() {
      return Asset.builder()
          .name(TestConfig.Asset.name)
          .tickerSymbol(TestConfig.Asset.tickerSymbol)
          .assetType(EntityTestFactory.AssetTypeFactory.create())
          .build();
    }

    public static Asset create(String name, String tickerSymbol) {
      return Asset.builder()
          .name(name)
          .tickerSymbol(tickerSymbol)
          .assetType(EntityTestFactory.AssetTypeFactory.create())
          .build();
    }
  }

  public static class PortfolioAssetFactory {
    public static PortfolioAsset create() {
      return PortfolioAsset.builder()
          .portfolio(EntityTestFactory.PortfolioFactory.create())
          .asset(EntityTestFactory.AssetFactory.create())
          .quantity(TestConfig.PortfolioAsset.quantity)
          .build();
    }

    public static PortfolioAsset create(Portfolio portfolio, Asset asset, BigDecimal quantity) {
      return PortfolioAsset.builder()
          .portfolio(portfolio)
          .asset(asset)
          .quantity(TestConfig.PortfolioAsset.quantity)
          .build();
    }
  }
}
