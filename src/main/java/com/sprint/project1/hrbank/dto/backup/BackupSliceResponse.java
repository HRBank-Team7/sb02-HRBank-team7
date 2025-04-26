package com.sprint.project1.hrbank.dto.backup;

import java.util.List;

public record BackupSliceResponse(
    List<BackupResponse> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    boolean hasNext
) {

}
