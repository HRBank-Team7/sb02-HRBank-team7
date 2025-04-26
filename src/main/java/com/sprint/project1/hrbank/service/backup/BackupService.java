package com.sprint.project1.hrbank.service.backup;

import com.sprint.project1.hrbank.dto.backup.BackupPagingRequest;
import com.sprint.project1.hrbank.dto.backup.BackupResponse;
import com.sprint.project1.hrbank.dto.backup.BackupSliceResponse;

public interface BackupService {
  BackupResponse triggerManualBackup(String worker);

  BackupSliceResponse searchBackups(BackupPagingRequest request);
}
