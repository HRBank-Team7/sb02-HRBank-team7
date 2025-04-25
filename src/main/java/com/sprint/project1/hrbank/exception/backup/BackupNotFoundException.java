package com.sprint.project1.hrbank.exception.backup;

public class BackupNotFoundException extends BackupException {

  public BackupNotFoundException(Long backupId) {
    super("백업 이력(id=" + backupId + ")을 찾을 수 없습니다.");
  }
}
