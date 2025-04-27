package com.sprint.project1.hrbank.service.backup;

import com.sprint.project1.hrbank.dto.backup.BackupPagingRequest;
import com.sprint.project1.hrbank.dto.backup.BackupResponse;
import com.sprint.project1.hrbank.dto.backup.BackupSliceResponse;
import com.sprint.project1.hrbank.entity.backup.BackupStatus;

public interface BackupService {
  BackupResponse triggerManualBackup(String worker);

  BackupSliceResponse searchBackups(BackupPagingRequest request);

  BackupResponse findLatestBackup(BackupStatus status);
}
