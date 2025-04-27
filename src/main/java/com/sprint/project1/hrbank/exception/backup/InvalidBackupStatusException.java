package com.sprint.project1.hrbank.exception.backup;

public class InvalidBackupStatusException extends BackupException {

  public InvalidBackupStatusException(String status) {
    super("지원하지 않는 백업 상태 입니다:" + status);
  }
}
