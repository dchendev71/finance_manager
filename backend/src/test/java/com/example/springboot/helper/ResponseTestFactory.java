package com.example.springboot.helper;

import com.example.springboot.asset.dto.AssetResponse;
import com.example.springboot.asset_type.dto.AssetTypeResponse;
import com.example.springboot.auth.dto.AuthResponse;
import com.example.springboot.config.TestConfig;
import com.example.springboot.portfolio.dto.PortfolioResponse;
import com.example.springboot.portfolio_asset.dto.PortfolioAssetResponse;
import com.example.springboot.user.dto.UserResponse;

public class ResponseTestFactory {
  public static class User {
    public static UserResponse create() {
      return UserResponse.builder()
          .email(TestConfig.User.email)
          .currency(EntityTestFactory.CurrencyFactory.createDto())
          .build();
    }

    public static UserResponse create(String email) {
      return UserResponse.builder()
          .email(email)
          .currency(EntityTestFactory.CurrencyFactory.createDto())
          .build();
    }
  }

  public static class Auth {
    public static AuthResponse create() {
      return new AuthResponse(TestConfig.Auth.jwt, ResponseTestFactory.User.create());
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

  public static class AssetType {
    public static AssetTypeResponse create() {
      return new AssetTypeResponse(TestConfig.AssetType.type);
    }
  }

  public static class Asset {
    public static AssetResponse create() {
      return new AssetResponse(
          TestConfig.Asset.name,
          TestConfig.Asset.tickerSymbol,
          ResponseTestFactory.AssetType.create());
    }
  }

  public static class PortfolioAsset {
    public static PortfolioAssetResponse create() {
      return new PortfolioAssetResponse(
          ResponseTestFactory.Portfolio.create(),
          ResponseTestFactory.Asset.create(),
          TestConfig.PortfolioAsset.quantity,
          TestConfig.PortfolioAsset.meanPrice);
    }
  }
}
