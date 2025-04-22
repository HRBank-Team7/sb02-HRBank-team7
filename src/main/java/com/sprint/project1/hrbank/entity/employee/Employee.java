package com.sprint.project1.hrbank.entity.employee;

import com.sprint.project1.hrbank.entity.base.BaseTimeEntity;
import com.sprint.project1.hrbank.entity.department.Department;
import com.sprint.project1.hrbank.entity.file.File;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employees")
public class Employee extends BaseTimeEntity {

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @OneToOne
  @JoinColumn(name = "profile_image_id")
  private File file;

  @Column(length = 50, nullable = false)
  private String name;

  @Column(length = 50, unique = true, nullable = false)
  private String email;

  @Column(length = 50, unique = true, nullable = false)
  private String employeeNumber;

  @Column(length = 100, nullable = false)
  private String position;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Instant hireDate;

  @Column
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private EmployeeStatus status;

}
