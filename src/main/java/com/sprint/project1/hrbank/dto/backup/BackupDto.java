package com.sprint.project1.hrbank.dto.backup;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record BackupDto(
    @NotNull
    Long id,
    @NotNull
    String worker,
    @NotNull
    Instant startedAt,
    Instant endedAt,
    @NotNull
    String status,
    Long fileId,
    String fileName
) {
}
