package com.example.springboot.common.exception;

public class ExistsException extends RuntimeException {
  public ExistsException(Class<?> className, String name) {
    super(className.getSimpleName() + ": " + name + " already exists!");
  }
}
