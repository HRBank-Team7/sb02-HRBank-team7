package com.sprint.project1.hrbank.controller.file;

import com.sprint.project1.hrbank.dto.file.FileCreateRequest;
import com.sprint.project1.hrbank.dto.file.FileMetadata;
import com.sprint.project1.hrbank.service.file.FileService;
import com.sprint.project1.hrbank.storage.FileStorage;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;


    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        FileMetadata fileMetadata = fileService.getMetadata(id);
        Resource resource;

        if (fileMetadata.name().startsWith("backups/")) {
            resource = fileService.downloadByName(id);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileMetadata.name() + "\"")
                .contentType(MediaType.parseMediaType(fileMetadata.type()))
                .contentLength(fileMetadata.size())
                .body(resource);
        } else {
            resource = fileService.download(id);

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileMetadata.name() + "\"")
                .contentType(MediaType.parseMediaType(fileMetadata.type()))
                .contentLength(fileMetadata.size())
                .body(resource);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        fileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
