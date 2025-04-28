package com.sprint.project1.hrbank.exception.employee;

import com.sprint.project1.hrbank.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("com.sprint.project1.hrbank")
public class EmployeeExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("EMPLOYEE_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(EmployeeProfileException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeProfileException(EmployeeProfileException ex) {
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of("FAIL TO PROCESS PROFILE", ex.getMessage()));
    }

    @ExceptionHandler(DuplicateEmployeeEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmployeeEmailException(DuplicateEmployeeEmailException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of("DUPLICATE EMAIL", ex.getMessage()));
    }

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeException(EmployeeException ex) {
        return ResponseEntity
                .internalServerError()
                .body(ErrorResponse.of("EMPLOYEE_ERROR", ex.getMessage()));
    }
}
