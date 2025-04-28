package com.sprint.project1.hrbank.exception.employee;

public class DuplicateEmployeeEmailException extends EmployeeException {
    public DuplicateEmployeeEmailException(String message) {
        super(message);
    }
}
