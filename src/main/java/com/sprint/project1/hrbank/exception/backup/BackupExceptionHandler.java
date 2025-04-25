package com.sprint.project1.hrbank.exception.backup;

import com.sprint.project1.hrbank.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.sprint.project1.hrbank.service.backup")
public class BackupExceptionHandler {

  @ExceptionHandler(BackupInProgressException.class)
  public ResponseEntity<ErrorResponse> handleBackupInProgress(BackupInProgressException ex) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(ErrorResponse.of("BACKUP_IN_PROGRESS", ex.getMessage()));
  }

  @ExceptionHandler(BackupNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(BackupNotFoundException ex) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ErrorResponse.of("BACKUP_NOT_FOUND", ex.getMessage()));
  }

  @ExceptionHandler(BackupFailureException.class)
  public ResponseEntity<ErrorResponse> handleFailure(BackupFailureException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of("BACKUP_FAILURE", ex.getMessage()));
  }

  @ExceptionHandler(BackupLogStorageException.class)
  public ResponseEntity<ErrorResponse> handleLogError(BackupLogStorageException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of("BACKUP_LOG_ERROR", ex.getMessage()));
  }

  @ExceptionHandler(BackupException.class)
  public ResponseEntity<ErrorResponse> handleBackupGeneric(BackupException ex) {
    log.error("Backup 예외 발생", ex);
    return ResponseEntity
        .internalServerError()
        .body(ErrorResponse.of("BACKUP_ERROR", ex.getMessage()));
  }
}
