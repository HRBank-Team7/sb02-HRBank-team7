package com.sprint.project1.hrbank.entity.backup;

import com.sprint.project1.hrbank.entity.base.BaseEntity;
import com.sprint.project1.hrbank.entity.file.File;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
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
@Table(name = "backups")
public class Backup extends BaseEntity {
  @Column(nullable = false)
  String worker;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  Instant startedAt;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  Instant endedAt;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  BackupStatus status;

  @OneToOne
  @JoinColumn(name = "file_id")
  File file;
}
