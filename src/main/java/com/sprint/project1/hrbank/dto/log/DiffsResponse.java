package com.sprint.project1.hrbank.dto.log;

import java.util.List;

public record DiffsResponse(
    List<DiffResponse> content
){}
