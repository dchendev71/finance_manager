package com.example.springboot.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // 409 - Email already exists
  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.CONFLICT, e);
  }

  // 404 - Ressource Not Found
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.NOT_FOUND, e);
  }

  // 400 - CurrencyNotFound
  @ExceptionHandler(CurrencyNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleCurrencyNotFound(CurrencyNotFoundException e) {
    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.BAD_REQUEST, e);
  }

  // 400 - Validation Failed
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
    String message =
        e.getBindingResult().getFieldErrors().stream()
            .map(err -> err.getField() + ":" + err.getDefaultMessage())
            .reduce("", (prevErr, currErr) -> prevErr + "; " + currErr);

    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.BAD_REQUEST, message);
  }
}
