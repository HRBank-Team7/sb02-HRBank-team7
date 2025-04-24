package com.sprint.project1.hrbank.service.backup;

import com.sprint.project1.hrbank.dto.backup.BackupDto;
public interface BackupService {
  BackupDto triggerManualBackup(String worker);
}
