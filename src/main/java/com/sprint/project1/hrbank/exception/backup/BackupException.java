package com.sprint.project1.hrbank.exception.backup;

public class BackupException extends RuntimeException {
  public BackupException(String message) {
    super(message);
  }

  public BackupException(String message, Throwable cause) {
    super(message, cause);
  }

}
