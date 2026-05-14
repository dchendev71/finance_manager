package com.example.springboot.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseEntityFactory {
  public static ResponseEntity<ErrorResponse> createResponseEntity(
      HttpStatus status, RuntimeException e) {
    return ResponseEntity.status(status).body(new ErrorResponse(status.value(), e.getMessage()));
  }

  public static ResponseEntity<ErrorResponse> createResponseEntity(
      HttpStatus status, String message) {
    return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
  }
}
