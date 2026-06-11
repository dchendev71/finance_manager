package com.example.springboot.common.exception;


public class InsufficientBalanceException extends RuntimeException {
  public InsufficientBalanceException() {
    super("Insufficient Balance");
  }
}
