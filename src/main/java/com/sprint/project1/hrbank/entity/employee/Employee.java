package com.sprint.project1.hrbank.entity.employee;

import com.sprint.project1.hrbank.dto.employee.EmployeeUpdateRequest;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.stereotype.Service;

@Setter
@Getter
@Service
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
  private LocalDate hireDate;

  @Column
  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private EmployeeStatus status = EmployeeStatus.ACTIVE;

  // 사번 생성
  public void generateEmployeeNumber(){
    LocalDateTime now = LocalDateTime.now();
    String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    String num = now.format(DateTimeFormatter.ofPattern("HHmmss"));
    this.employeeNumber = String.format("EMP-%s-%s",date, num);
  }

  public void assignDepartment(Department department){
    this.department = department;
  }

  public void assignFile(File file) {
    this.file = file;
  }

  public Employee update(EmployeeUpdateRequest request, Department department, File file){
    if(request.name()!=null && !request.name().equals(this.name)) {
      this.name = request.name();
    }
    if(request.email()!=null && !request.email().equals(this.email)){
      this.email = request.email();
    }
    if(request.position()!=null && !request.position().equals(this.position)){
      this.position = request.position();
    }
    if(request.hireDate()!=null && !request.hireDate().equals(this.hireDate)){
      this.hireDate = request.hireDate();
    }
    if(request.status()!=null && !request.status().equals(this.status)){
      this.status = request.status();
    }
    if(request.departmentId() != null && !request.departmentId().equals(this.department.getId())){
      this.department = department;
    }
    this.file = file;

    return this;
  }

  // 대시 보드 trend 날짜 지정
  public static List<LocalDate> generateDatePointTrend(LocalDate from, LocalDate to, String unit) {
    List<LocalDate> datePoints = new ArrayList<>();

    ChronoUnit chronoUnit;
    switch (unit) {
      case "day" -> chronoUnit = ChronoUnit.DAYS;
      case "week" -> chronoUnit = ChronoUnit.WEEKS;
      case "month" -> chronoUnit = ChronoUnit.MONTHS;
      case "quarter" -> chronoUnit = ChronoUnit.MONTHS;
      case "year" -> chronoUnit = ChronoUnit.YEARS;
      default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
    }
    int uniSize = unit.equals("quarter") ? 3 : 1;

    LocalDate end = (to != null) ? to : LocalDate.now();
    LocalDate start = (from != null) ? from : end.minus(uniSize * 12, chronoUnit);

    for (LocalDate date = start; !date.isAfter(end); date = date.plus(uniSize, chronoUnit)) {
      datePoints.add(date);
    }
    return datePoints;
  }
}
