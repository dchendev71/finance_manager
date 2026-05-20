package com.example.springboot.common.exception;

public class NotFoundException extends RuntimeException {
  public NotFoundException(Class<?> className, String name) {
    super(className.getSimpleName() + ": " + name + ": not found!");
  }
}
