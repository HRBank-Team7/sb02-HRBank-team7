package com.sprint.project1.hrbank.repository.backup;

import com.sprint.project1.hrbank.entity.backup.Backup;
import com.sprint.project1.hrbank.entity.backup.BackupStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BackupRepository extends JpaRepository<Backup, Long> {

  Optional<Backup> findTopByStatusOrderByEndedAtDesc(BackupStatus status);

  Optional<Backup> findTopByStatus(BackupStatus status);
}
