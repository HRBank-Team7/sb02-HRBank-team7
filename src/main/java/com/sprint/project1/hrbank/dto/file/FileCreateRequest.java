package com.sprint.project1.hrbank.dto.file;

public record FileCreateRequest(
        String name,
        String type,
        Long size,
        byte[] bytes
) {
}
