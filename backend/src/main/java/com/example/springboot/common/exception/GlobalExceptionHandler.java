package com.example.springboot.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

  // 401 - Invalid Credentials
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException e) {
    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.UNAUTHORIZED, e);
  }

  // 404 - User Not Found
  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException e) {
    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.NOT_FOUND, e);
  }

  @ExceptionHandler(PasswordNotMatchException.class)
  public ResponseEntity<ErrorResponse> handlePasswordNotMatch(PasswordNotMatchException e) {
    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.UNAUTHORIZED, e);
  }

  @ExceptionHandler({DisabledException.class, InternalAuthenticationServiceException.class})
  public ResponseEntity<ErrorResponse> handleDisabledException(DisabledException e) {
    return ErrorResponseEntityFactory.createResponseEntity(HttpStatus.UNAUTHORIZED, e);
  }
}
