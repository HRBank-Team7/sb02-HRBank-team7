package com.sprint.project1.hrbank.repository.file;

import com.sprint.project1.hrbank.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File,Long> {
}
