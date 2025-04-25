package com.sprint.project1.hrbank.service.backup;

import com.sprint.project1.hrbank.dto.backup.BackupDto;
import com.sprint.project1.hrbank.dto.file.FileCreateRequest;
import com.sprint.project1.hrbank.entity.backup.Backup;
import com.sprint.project1.hrbank.entity.backup.BackupStatus;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.file.File;
import com.sprint.project1.hrbank.infrastructure.EmployeeCsvGenerator;
import com.sprint.project1.hrbank.infrastructure.ErrorLogWriter;
import com.sprint.project1.hrbank.mapper.backup.BackupMapper;
import com.sprint.project1.hrbank.repository.backup.BackupRepository;
import com.sprint.project1.hrbank.repository.employee.EmployeeRepository;
import com.sprint.project1.hrbank.service.file.FileService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl implements BackupService {

  private final BackupRepository backupRepository;
  private final EmployeeRepository employeeRepository;
  private final FileService fileService;
  private final BackupMapper backupMapper;
  private final EmployeeCsvGenerator employeeCsvGenerator;
  private final ErrorLogWriter errorLogWriter;

  /**
   * [직원 백업 프로세스 전체 흐름]
   * 1. 현재 진행 중인 백업이 있는지 확인 (중복 방지)
   * 2. 마지막 완료된 백업 시점 이후로 변경된 직원이 있는지 확인
   * 3. 백업 이력(IN_PROGRESS) 생성 및 저장
   * 4. 변경사항 없으면 SKIPPED 처리 후 종료
   * 5. 변경사항 있으면 직원 전체 조회 후 CSV 생성
   * 6. 생성한 CSV를 파일로 저장하고 메타데이터 저장
   * 7. 백업 이력을 COMPLETED로 마무리하고 연결된 파일 등록
   * 8. 예외 발생 시 로그 바이트 생성 → 파일 저장 → 백업 이력을 FAILED로 마무리
   * 9. 최종적으로 백업 이력 정보를 DTO로 변환해 반환
   */
  @Override
  @Transactional
  public BackupDto triggerManualBackup(String workerIp) {
    backupRepository.findTopByStatus(BackupStatus.IN_PROGRESS).ifPresent(b -> {
      throw new IllegalStateException("이미 진행중인 백업 작업이 존재합니다.");
    });

    Optional<Instant> optionalLastBackupTime = backupRepository.findTopByStatusOrderByEndedAtDesc(BackupStatus.COMPLETED)
        .map(Backup::getEndedAt);
    Instant lastBackupTime = optionalLastBackupTime.orElse(Instant.EPOCH);

    boolean needsBackup = employeeRepository.existsByUpdatedAtAfter(lastBackupTime);

    Backup backup = new Backup(workerIp);
    backupRepository.save(backup);

    if (!needsBackup) {
      log.info("No employee changes since last backup. Skipping...");
      backup.skip();
      return backupMapper.toDto(backup);
    }

    try {
      List<Employee> employees = employeeRepository.findAll();
      String csvFilename = "backups/employee_backup_" + System.currentTimeMillis() + ".csv";
      byte[] csvBytes = employeeCsvGenerator.generateCsvBytes(employees);

      FileCreateRequest request = new FileCreateRequest(
          csvFilename,
          "text/csv",
          (long) csvBytes.length,
          csvBytes
      );
      File savedFile = fileService.create(request, csvFilename);
      backup.complete(savedFile);
      log.info("Backup completed successfully: {}", savedFile.getName());

    } catch (Exception ex) {
      log.error("Backup failed", ex);
      try {
        byte[] errorBytes = errorLogWriter.generateLogBytes(ex);
        String logFilename = "logs/backup_error_" + System.currentTimeMillis() + ".log";

        FileCreateRequest errorRequest = new FileCreateRequest(
            logFilename,
            "text/plain",
            (long) errorBytes.length,
            errorBytes
        );
        File errorLog = fileService.create(errorRequest, logFilename);
        backup.fail(errorLog);
      } catch (IOException ioEx) {
        throw new RuntimeException("로그 파일 저장 중 오류 발생", ioEx);
      }
    }

    return backupMapper.toDto(backup);
  }
}
