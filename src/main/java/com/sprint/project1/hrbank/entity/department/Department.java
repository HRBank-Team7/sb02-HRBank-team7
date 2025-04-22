package com.sprint.project1.hrbank.entity.department;

import com.sprint.project1.hrbank.entity.base.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department extends BaseTimeEntity {

  @Column(length=50, nullable = false)
  private String name;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Instant establishedDate;

  @Column
  private String description;



}
