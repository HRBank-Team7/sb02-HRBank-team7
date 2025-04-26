package com.sprint.project1.hrbank.exception.file;

import com.sprint.project1.hrbank.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.sprint.project1.hrbank")
public class FileExceptionHandler {

  @ExceptionHandler(FileNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleFileNotFound(FileNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of("FILE_NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(FileException.class)
  public ResponseEntity<ErrorResponse> handleFileGeneric(FileException ex){
    return ResponseEntity
        .internalServerError()
        .body(ErrorResponse.of("FILE_ERROR", ex.getMessage()));
  }
}
