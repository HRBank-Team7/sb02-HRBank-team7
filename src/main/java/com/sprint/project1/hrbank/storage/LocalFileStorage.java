package com.sprint.project1.hrbank.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@ConditionalOnProperty(name = "hrbank.storage.type", havingValue = "local")
public class LocalFileStorage implements FileStorage {
    private final Path root;

    public LocalFileStorage(
            @Value("${hrbank.storage.local.root-path}") String defaultPath) {
        this.root = Paths.get(defaultPath);
    }

    @PostConstruct
    private void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("디렉토리 생성에 실패하였습니다.: " + root, e);
        }
    }

    @Override
    public Long put(Long fileId, byte[] bytes) {
        Path path = resolvePath(fileId);
        try{
            Files.write(path, bytes);
            return fileId;
        }catch (IOException e){
            throw new RuntimeException("파일 작성에 실패하였습니다.: " + path, e);
        }
    }

    @Override
    public void put(String filename, byte[] bytes) {
        Path path = resolvePath(filename);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new RuntimeException("파일 작성에 실패하였습니다.: " + path, e);
        }
    }

    @Override
    public InputStream get(Long fileId) {
        Path path = resolvePath(fileId);
        try{
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기에 실패하였습니다.: " + path, e);
        }
    }

    @Override
    public Resource download(Long fileId) {
        InputStream inputStream = get(fileId);
        Resource resource = new InputStreamResource(inputStream);

        return resource;
    }



    @Override
    public Resource download(String filename) {
        Path path = resolvePath(filename);
        try {
            InputStream inputStream = Files.newInputStream(path);
            return new InputStreamResource(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("파일 다운로드 실패: " + path, e);
        }
    }

    @Override
    public void delete(Long fileId) {
        try {
            Files.deleteIfExists(resolvePath(fileId));
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제에 실패하였습니다.:" + e);
        }
    }

    private Path resolvePath(Long fileId) {
        return root.resolve(fileId.toString());
    }
    private Path resolvePath(String filename) {
        return root.resolve(filename);
    }
}
