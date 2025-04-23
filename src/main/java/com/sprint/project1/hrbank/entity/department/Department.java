package com.sprint.project1.hrbank.entity.department;

import com.sprint.project1.hrbank.entity.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments")
public class Department extends BaseTimeEntity {

  @Column(length=50, nullable = false)
  private String name;

  @Column(nullable = false)
  private Instant establishedDate;

  @Column
  private String description;

  public void validateNotDuplicateWith(Department other) {
    if (other == null) {
      return;
    }

    if (this.name.equals(other.getName())) {
      throw new IllegalArgumentException("중복된 부서 이름입니다");
    }
  }

}
