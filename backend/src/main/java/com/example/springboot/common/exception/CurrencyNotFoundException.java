package com.example.springboot.common.exception;

public class CurrencyNotFoundException extends RuntimeException {
  public CurrencyNotFoundException(String currencyCode) {
    super("Currency not found: " + currencyCode);
  }
}
