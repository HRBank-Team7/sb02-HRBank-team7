package com.sprint.project1.hrbank.exception.backup;

public class BackupFailureException extends RuntimeException {

  public BackupFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
