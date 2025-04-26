package com.sprint.project1.hrbank.repository.backup;

import com.sprint.project1.hrbank.dto.backup.BackupPagingRequest;
import com.sprint.project1.hrbank.entity.backup.Backup;
import java.util.List;

public interface BackupRepositoryCustom {
  List<Backup> search(BackupPagingRequest request);
}
