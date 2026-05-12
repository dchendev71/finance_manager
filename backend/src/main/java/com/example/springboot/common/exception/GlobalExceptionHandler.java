package com.example.springboot.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
  public ProblemDetail handleIllegalArgument(IllegalArgumentException e) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

    problemDetail.setTitle("Invalid Request Content");

    return problemDetail;
  }
}
