package com.sprint.project1.hrbank.exception.log;

import com.sprint.project1.hrbank.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.sprint.project1.hrbank")
public class EmployeeLogExceptionHandler {

  @ExceptionHandler(EmployeeLogException.class)
  public ResponseEntity<ErrorResponse> handleEmployeeLogNotFound(EmployeeLogNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of("EMPLOYEE_LOG_NOT_FOUND", ex.getMessage()));
  }

}
