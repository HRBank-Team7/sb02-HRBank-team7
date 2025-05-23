package com.sprint.project1.hrbank.service.file;

import com.sprint.project1.hrbank.dto.file.FileCreateRequest;
import com.sprint.project1.hrbank.dto.file.FileMetadata;
import com.sprint.project1.hrbank.entity.file.File;
import com.sprint.project1.hrbank.exception.file.FileNotFoundException;
import com.sprint.project1.hrbank.mapper.file.FileMapper;
import com.sprint.project1.hrbank.repository.file.FileRepository;
import com.sprint.project1.hrbank.storage.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;
    private final FileStorage fileStorage;

    @Override
    @Transactional
    public File create(FileCreateRequest request) {
        File file = new File(request.name(), request.type(), request.size());
        fileRepository.save(file);
        fileStorage.put(file.getId(), request.bytes());
        return file;
    }

    @Override
    @Transactional
    public File create(FileCreateRequest request, String filePath) {
        File file = new File(request.name(), request.type(), request.size());
        fileRepository.save(file);
        if (request.bytes() != null) {
            fileStorage.put(filePath, request.bytes());
        }
        return file;
    }

    @Override
    public Resource download(Long fileId) {
        return fileStorage.download(fileId);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource downloadByName(Long fileId) {
        File file = fileRepository.findById(fileId)
            .orElseThrow(() -> new FileNotFoundException("파일을 찾을 수 없습니다."));

        return fileStorage.download(file.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public FileMetadata getMetadata(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFoundException("파일을 찾을 수 없습니다."));
        return fileMapper.toMetadata(file);
    }

    @Override
    @Transactional
    public void delete(Long fileId) {
        if (!fileRepository.existsById(fileId)){
            throw new FileNotFoundException("파일을 찾을 수 없습니다.");
        }
        fileRepository.deleteById(fileId);
        fileStorage.delete(fileId);
    }
}
