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

  public Employee update(EmployeeUpdateRequest request, Department department /*File file*/){
    if(request.name()!=null && !this.name.equals(request.name())) {
      this.name = request.name();
    }
    if(request.email()!=null && !this.email.equals(request.email())){
      this.email = request.email();
    }
    if(request.position()!=null && !this.position.equals(request.position())){
      this.position = request.position();
    }
    if(request.hireDate()!=null && !this.hireDate.equals(request.hireDate())){
      this.hireDate = request.hireDate();
    }
    if(request.status()!=null && !this.status.equals(request.status())){
      this.status = request.status();
    }
    if(request.departmentId()!=null && !this.department.getId().equals(request.departmentId())){
      this.department = department;
    }
//     file 나중에 해야함
//    if(file != null && this.file!=file){
//      this.file = file;
//    }
    return this;
  }

}
