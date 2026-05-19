package com.example.springboot.common.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountDeletedException extends AuthenticationException {
  public AccountDeletedException() {
    super("This account has been deleted");
  }
}
