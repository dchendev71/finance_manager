package com.example.springboot.helper;

import com.example.springboot.currency.Currency;
import com.example.springboot.currency.dto.CurrencyDto;

public class CurrencyTestFactory {
  public static Long testId = 1L;
  public static String testCode = "USD";
  public static String testName = "US Dollar";
  public static String testSymbol = "$";

  public static Currency createCurrency() {
    return Currency.builder().id(testId).code(testCode).name(testName).symbol(testSymbol).build();
  }

  public static Currency createCurrency(String code, String name, String symbol) {
    return Currency.builder().code(code).name(name).symbol(symbol).build();
  }

  public static CurrencyDto createCurrencyDto() {
    return CurrencyDto.builder().code(testCode).name(testName).symbol(testSymbol).build();
  }

  public static CurrencyDto createCurrencyDto(String code, String name, String symbol) {
    return CurrencyDto.builder().code(code).name(name).symbol(symbol).build();
  }
}
