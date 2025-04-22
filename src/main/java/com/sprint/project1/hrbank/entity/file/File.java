package com.sprint.project1.hrbank.entity.file;

import com.sprint.project1.hrbank.entity.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class File extends BaseTimeEntity {

  @Column(length = 100, nullable = false)
  private String name;

  @Column(length = 100, nullable = false)
  private String type;

  @Column(nullable = false)
  private Long size;


}