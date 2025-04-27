package com.sprint.project1.hrbank.entity.log;

import com.sprint.project1.hrbank.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import java.time.Instant;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_change_logs")
public class EmployeeLog extends BaseEntity{

  @Column(length = 100, nullable = false)
  private String type;

  @Column(length = 50, nullable = false)
  private String employeeNumber;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<EmployeeDiff> employeeDiffs;

  @Column
  private String memo;

  @Column(length = 50, nullable = false)
  private String ipAddress;

  @Column(nullable = false)
  private Instant at;
}

