package com.sprint.project1.hrbank.exception.backup;

public class BackupInProgressException extends BackupException {

  public BackupInProgressException() {
    super("이미 진행 중인 백업이 존재합니다.");
  }
}
