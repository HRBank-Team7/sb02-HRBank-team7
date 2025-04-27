package com.sprint.project1.hrbank.exception.backup;

import com.sprint.project1.hrbank.entity.backup.BackupStatus;

public class BackupNotFoundException extends BackupException {

  public BackupNotFoundException(Long backupId) {
    super("백업 이력(id=" + backupId + ")을 찾을 수 없습니다.");
  }

  public BackupNotFoundException(BackupStatus status) {
    super("백업 상태(" + status + ")에 해당하는 최신 백업 이력이 존재하지 않습니다.");
  }
}
