package com.example.springboot.helper;

import com.example.springboot.auth.dto.UserCreateRequest;
import com.example.springboot.currency.Currency;
import com.example.springboot.user.User;
import com.example.springboot.user.dto.ChangeEmailRequest;
import com.example.springboot.user.dto.ChangePasswordRequest;
import com.example.springboot.user.dto.UserResponse;

public class UserTestFactory {
  public static String testPassword = "password123";
  public static Long testId = 1L;
  public static String testEmail = "john@example.com";
  public static String testCurrencyCode = "USD";

  public static User createUser() {
    return User.builder()
        .id(testId)
        .email(testEmail)
        .password(testPassword)
        .active(true)
        .currency(CurrencyTestFactory.createCurrency())
        .build();
  }

  public static User createUser(String email, Currency currency) {
    return User.builder()
        .id(testId)
        .email(email)
        .password(testPassword)
        .active(true)
        .currency(currency)
        .build();
  }

  public static UserCreateRequest createUserRequest() {
    return UserCreateRequest.builder()
        .email(testEmail)
        .password(testPassword)
        .currencyCode(testCurrencyCode)
        .build();
  }

  public static UserCreateRequest createUserRequest(
      String email, String password, String currencyCode) {
    return UserCreateRequest.builder()
        .email(email)
        .password(password)
        .currencyCode(currencyCode)
        .build();
  }

  public static UserResponse createUserResponse() {
    return UserResponse.builder()
        .id(testId)
        .email(testEmail)
        .currency(CurrencyTestFactory.createCurrencyDto())
        .createdAt(null)
        .updatedAt(null)
        .build();
  }

  public static ChangePasswordRequest createChangePasswordRequest(String newPassword) {
    return ChangePasswordRequest.builder()
        .currentPassword(testPassword)
        .newPassword(newPassword)
        .confirmPassword(newPassword)
        .build();
  }

  public static ChangeEmailRequest createChangeEmailRequest(String newEmail) {
    return ChangeEmailRequest.builder().currentPassword(testPassword).newEmail(newEmail).build();
  }
}
