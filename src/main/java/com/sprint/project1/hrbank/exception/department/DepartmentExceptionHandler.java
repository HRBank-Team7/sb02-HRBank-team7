package com.sprint.project1.hrbank.exception.department;

import com.sprint.project1.hrbank.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.sprint.project1.hrbank")
public class DepartmentExceptionHandler {

  @ExceptionHandler(DepartmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleDepartmentNotFound(DepartmentNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of("DEPARTMENT_NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(DepartmentDeletionNotAllowedException.class)
  public ResponseEntity<ErrorResponse> handleDepartmentDeletionNotAllowed(DepartmentDeletionNotAllowedException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ErrorResponse.of("DEPARTMENT_DELETION_NOT_ALLOWED", ex.getMessage()));
  }

  @ExceptionHandler(DuplicateDepartmentException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateDepartmentException(DuplicateDepartmentException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ErrorResponse.of("DUPLICATE_DEPARTMENT", ex.getMessage()));
  }

  @ExceptionHandler(DepartmentException.class)
  public ResponseEntity<ErrorResponse> handleDepartmentGeneric(DepartmentException ex){
    return ResponseEntity
        .internalServerError()
        .body(ErrorResponse.of("DEPARTMENT_ERROR", ex.getMessage()));
  }
}
