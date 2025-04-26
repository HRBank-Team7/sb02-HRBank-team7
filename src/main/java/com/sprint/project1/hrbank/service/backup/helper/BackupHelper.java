package com.sprint.project1.hrbank.service.backup.helper;

import com.sprint.project1.hrbank.common.MimeTypes;
import com.sprint.project1.hrbank.dto.file.FileCreateRequest;
import com.sprint.project1.hrbank.entity.employee.Employee;
import com.sprint.project1.hrbank.entity.file.File;
import com.sprint.project1.hrbank.infrastructure.EmployeeCsvGenerator;
import com.sprint.project1.hrbank.infrastructure.ErrorLogWriter;
import com.sprint.project1.hrbank.repository.employee.EmployeeRepository;
import com.sprint.project1.hrbank.service.file.FileService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BackupHelper {

  private final EmployeeRepository employeeRepository;
  private final EmployeeCsvGenerator employeeCsvGenerator;
  private final ErrorLogWriter errorLogWriter;
  private final FileService fileService;

  public File backupEmployees() throws IOException {
    List<Employee> employees = employeeRepository.findAll();
    byte[] csvBytes = employeeCsvGenerator.generateCsvBytes(employees);

    FileCreateRequest request = new FileCreateRequest(
        generateCsvFilename(),
        MimeTypes.CSV,
        (long) csvBytes.length,
        csvBytes
    );
    return fileService.create(request, request.name());
  }

  public File writeBackupErrorLog(Exception ex) throws IOException {
    byte[] errorBytes = errorLogWriter.generateLogBytes(ex);

    FileCreateRequest errorRequest = new FileCreateRequest(
        generateLogFilename(),
        MimeTypes.PLAIN_TEXT,
        (long) errorBytes.length,
        errorBytes
    );
    return fileService.create(errorRequest, errorRequest.name());
  }

  private String generateCsvFilename() {
    return "backups/employee_backup_" + System.currentTimeMillis() + ".csv";
  }

  private String generateLogFilename() {
    return "logs/backup_error_" + System.currentTimeMillis() + ".log";
  }
}
