package com.example.springboot.common.exception;

public class PasswordNotMatchException extends RuntimeException {
  public PasswordNotMatchException() {
    super("Password doesn't match");
  }
}
