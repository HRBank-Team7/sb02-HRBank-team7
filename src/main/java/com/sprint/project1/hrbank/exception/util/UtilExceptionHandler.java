package com.sprint.project1.hrbank.exception.util;

import com.sprint.project1.hrbank.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.sprint.project1.hrbank")
public class UtilExceptionHandler {

  @ExceptionHandler(InvalidCursorFormatException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCursorFormat(InvalidCursorFormatException ex){
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ErrorResponse.of("INVALID_CURSOR_FORMAT", ex.getMessage()));
  }
  @ExceptionHandler(UtilException.class)
  public ResponseEntity<ErrorResponse> handleUtilGeneric(UtilException ex){
    return ResponseEntity
        .internalServerError()
        .body(ErrorResponse.of("UTIL_ERROR", ex.getMessage()));
  }

}