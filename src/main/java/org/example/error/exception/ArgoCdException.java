package org.example.error.exception;

import lombok.Getter;

@Getter
public class ArgoCdException extends RuntimeException {

  private final String detail;

  public ArgoCdException(String message) {
    super(message);
    this.detail = "%s.%s".formatted(Thread.currentThread().getStackTrace()[2].getClassName(),
        Thread.currentThread().getStackTrace()[2].getMethodName());
  }
}
