package com.sprint.project1.hrbank.controller.backup;

import com.sprint.project1.hrbank.dto.backup.BackupPagingRequest;
import com.sprint.project1.hrbank.dto.backup.BackupResponse;
import com.sprint.project1.hrbank.dto.backup.BackupSliceResponse;
import com.sprint.project1.hrbank.entity.backup.BackupStatus;
import com.sprint.project1.hrbank.service.backup.BackupService;
import com.sprint.project1.hrbank.util.ClientIpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/backups")
@RequiredArgsConstructor
public class BackupController {

  private final BackupService backupService;
  private final ClientIpUtil clientIpUtil;

  @PostMapping
  public ResponseEntity<BackupResponse> createBackups(HttpServletRequest request) {
    String workerIp = clientIpUtil.getClientIp(request);
    BackupResponse result = backupService.triggerManualBackup(workerIp);
    return ResponseEntity.ok(result);
  }

  @GetMapping
  ResponseEntity<BackupSliceResponse> searchBackups(@Valid @ModelAttribute BackupPagingRequest request) {
    BackupSliceResponse result = backupService.searchBackups(request);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/latest")
  public BackupResponse getLatestBackup(@RequestParam(value = "status", required = false) BackupStatus status) {
    return backupService.findLatestBackup(status);
  }
}
