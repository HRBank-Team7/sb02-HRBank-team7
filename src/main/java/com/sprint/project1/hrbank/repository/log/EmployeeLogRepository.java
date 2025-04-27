package com.sprint.project1.hrbank.repository.log;

import com.sprint.project1.hrbank.entity.log.EmployeeLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeLogRepository extends JpaRepository<EmployeeLog, Long>, EmployeeLogRepositoryCustom {
}
