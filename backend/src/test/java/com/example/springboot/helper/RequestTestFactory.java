package com.example.springboot.helper;

import com.example.springboot.auth.dto.AuthRequest;
import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.config.TestConfig;
import com.example.springboot.portfolio.dto.PortfolioCreateRequest;
import com.example.springboot.portfolio.portfolio_asset.dto.PortfolioAssetRequest;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import java.math.BigDecimal;

public class RequestTestFactory {
  public static class User {
    public static UserCreateRequest register() {
      return UserCreateRequest.builder()
          .email(TestConfig.User.email)
          .password(TestConfig.User.password)
          .currencyCode(TestConfig.Currency.code)
          .build();
    }

    public static UserCreateRequest register(String email, String password, String currencyCode) {
      return UserCreateRequest.builder()
          .email(email)
          .password(password)
          .currencyCode(currencyCode)
          .build();
    }

    public static ChangePasswordRequest changePassword(String newPassword) {
      return ChangePasswordRequest.builder()
          .currentPassword(TestConfig.User.password)
          .newPassword(newPassword)
          .confirmPassword(newPassword)
          .build();
    }

    public static ChangePasswordRequest changePassword(String currentPassword, String newPassword) {
      return ChangePasswordRequest.builder()
          .currentPassword(currentPassword)
          .newPassword(newPassword)
          .confirmPassword(newPassword)
          .build();
    }

    public static ChangeEmailRequest changeEmail(String newEmail) {
      return ChangeEmailRequest.builder()
          .currentPassword(TestConfig.User.password)
          .newEmail(newEmail)
          .build();
    }

    public static ChangeEmailRequest changeEmail(String currentPassword, String newEmail) {
      return ChangeEmailRequest.builder()
          .currentPassword(currentPassword)
          .newEmail(newEmail)
          .build();
    }
  }

  public static class Auth {
    public static AuthRequest login() {
      return AuthRequest.builder()
          .email(TestConfig.User.email)
          .password(TestConfig.User.password)
          .build();
    }

    public static AuthRequest login(String email) {
      return AuthRequest.builder().email(email).password(TestConfig.User.password).build();
    }

    public static AuthRequest login(String email, String password) {
      return AuthRequest.builder().email(email).password(password).build();
    }
  }

  public static class Portfolio {
    public static PortfolioCreateRequest create() {
      return PortfolioCreateRequest.builder().portfolioName(TestConfig.Portfolio.name).build();
    }

    public static PortfolioCreateRequest create(String portfolioName) {
      return PortfolioCreateRequest.builder().portfolioName(portfolioName).build();
    }
  }

  public static class PortfolioAsset {

    public static PortfolioAssetRequest create(
        String portfolioName, String assetName, BigDecimal quantity) {
      return new PortfolioAssetRequest(
          portfolioName, assetName, quantity, TestConfig.PortfolioAsset.price);
    }

    public static PortfolioAssetRequest create() {
      return new PortfolioAssetRequest(
          TestConfig.Portfolio.name,
          TestConfig.Asset.name,
          TestConfig.PortfolioAsset.quantity,
          TestConfig.PortfolioAsset.price);
    }

    public static PortfolioAssetRequest create(BigDecimal quantity) {
      return new PortfolioAssetRequest(
          TestConfig.Portfolio.name,
          TestConfig.Asset.name,
          quantity,
          TestConfig.PortfolioAsset.price);
    }
  }
}
