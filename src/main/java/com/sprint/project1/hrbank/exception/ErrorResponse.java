package com.sprint.project1.hrbank.exception;

public record ErrorResponse(
    boolean success,
    String error,
    String message
) {
  public static ErrorResponse of(String error, String message) {
    return new ErrorResponse(false, error, message);
  }
}
