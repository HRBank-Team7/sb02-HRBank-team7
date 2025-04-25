package com.sprint.project1.hrbank.controller.backup;

import com.sprint.project1.hrbank.dto.backup.BackupDto;
import com.sprint.project1.hrbank.service.backup.BackupService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController {

  private final BackupService backupService;

  @PostMapping
  public ResponseEntity<BackupDto> createBackup(HttpServletRequest request) {
    String workerIp = request.getRemoteAddr();
    BackupDto result = backupService.triggerManualBackup(workerIp);
    return ResponseEntity.ok(result);
  }
}
