package com.sprint.project1.hrbank.dto.backup;

import com.sprint.project1.hrbank.entity.backup.BackupStatus;
import java.time.Instant;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record BackupPagingRequest(
    String worker,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant startedFrom,
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    Instant startedTo,
    BackupStatus status,
    BackupSortBy sortBy,
    Integer size,
    String cursor
) {
}
