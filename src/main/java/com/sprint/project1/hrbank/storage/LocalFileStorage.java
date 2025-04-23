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
            throw new RuntimeException("Failed to create directory: " + root, e);
        }
    }

    @Override
    public Long put(Long fileId, byte[] bytes) {
        Path path = resolvePath(fileId);
        try{
            Files.write(path, bytes);
            return fileId;
        }catch (IOException e){
            throw new RuntimeException("Failed to write file: " + path, e);
        }
    }

    @Override
    public InputStream get(Long fileId) {
        Path path = resolvePath(fileId);
        try{
            return Files.newInputStream(path);
        }catch(IOException e){
            throw new RuntimeException("Failed to read file: " + path, e);
        }
    }

    @Override
    public Resource download(Long fileId) {
        InputStream inputStream = get(fileId);
        Resource resource = new InputStreamResource(inputStream);

        return resource;
    }

    private Path resolvePath(Long fileId) {
        return root.resolve(fileId.toString());
    }
}
