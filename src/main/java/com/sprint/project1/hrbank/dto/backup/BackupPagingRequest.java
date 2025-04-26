package com.sprint.project1.hrbank.dto.backup;

import com.sprint.project1.hrbank.entity.backup.BackupStatus;
import java.time.Instant;
import lombok.Builder;

@Builder
public record BackupPagingRequest(
    String worker,
    Instant startedAtFrom,
    Instant startedAtTo,
    BackupStatus status,
    Integer size,
    Long idAfter,
    String cursor,
    BackupSortField sortField,
    BackupSortDirection sortDirection
) {
}
