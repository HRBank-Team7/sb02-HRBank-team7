package com.sprint.project1.hrbank.service.file;

import com.sprint.project1.hrbank.dto.file.FileCreateRequest;
import com.sprint.project1.hrbank.dto.file.FileMetadata;
import com.sprint.project1.hrbank.entity.file.File;
import org.springframework.core.io.Resource;

public interface FileService {
    File create(FileCreateRequest request);
    File create(FileCreateRequest request, String filePath);
    Resource download (Long fileId);
    FileMetadata getMetadata(Long fileId);
}
