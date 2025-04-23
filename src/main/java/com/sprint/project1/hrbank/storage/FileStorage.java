package com.sprint.project1.hrbank.storage;

import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface FileStorage {
    Long put(Long fileId, byte[] bytes);
    InputStream get(Long fileId);
    Resource download(Long fileId);
}
