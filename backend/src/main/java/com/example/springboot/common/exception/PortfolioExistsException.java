package com.example.springboot.common.exception;

public class PortfolioExistsException extends RuntimeException {
  public PortfolioExistsException(String portfolioName) {
    super(portfolioName + " already exists!");
  }
}
