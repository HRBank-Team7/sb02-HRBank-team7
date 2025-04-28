package com.sprint.project1.hrbank.scheduler;

import com.sprint.project1.hrbank.service.backup.BackupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BackupScheduler {

  private final BackupService backupService;

  @Scheduled(cron = "${backup.cron}")
  public void runBackup() {
    try {
      log.info("시스템 백업 스케줄러 실행");

      backupService.triggerManualBackup("system");
      log.info("시스템 백업 스케줄러 실행 완료");
    } catch (Exception e) {
      log.error("시스템 백업 스케줄러 실패", e);
    }
  }
}
