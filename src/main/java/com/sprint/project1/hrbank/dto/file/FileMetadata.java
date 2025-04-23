package com.sprint.project1.hrbank.dto.file;

public record FileMetadata(
        Long id,
        String name,
        String type,
        Long size
){
}
