package com.sprint.project1.hrbank.dto.backup;

import com.sprint.project1.hrbank.entity.backup.BackupStatus;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record BackupResponse(
    @NotNull
    Long id,
    @NotNull
    String worker,
    @NotNull
    Instant startedAt,
    Instant endedAt,
    @NotNull
    BackupStatus status,
    Long fileId,
    String fileName
) {
}
