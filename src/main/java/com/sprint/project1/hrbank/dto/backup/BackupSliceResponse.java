package com.sprint.project1.hrbank.dto.backup;

import java.util.List;

public record BackupSliceResponse(
    List<BackupResponse> contents,
    String nextCursor
) {

}
