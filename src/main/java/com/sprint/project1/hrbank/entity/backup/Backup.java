package com.sprint.project1.hrbank.entity.backup;

import com.sprint.project1.hrbank.entity.base.BaseEntity;
import com.sprint.project1.hrbank.entity.file.File;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "backups")
public class Backup extends BaseEntity {
  @Column(length = 50, nullable = false)
  private String worker;

  @Column(nullable = false)
  private Instant startedAt;

  @Column(nullable = true)
  private Instant endedAt;

  @Column(length = 20, nullable = false)
  @Enumerated(value = EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private BackupStatus status;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  private File file;

  public Backup(String worker) {
    this.worker = worker;
    start();
  }

  public void start() {
    this.status = BackupStatus.IN_PROGRESS;
    this.startedAt = Instant.now();
    this.endedAt = null;
    this.file = null;
  }

  public void complete(File completedFile) {
    if (this.status != BackupStatus.IN_PROGRESS) {
       throw new IllegalStateException("백업 이력은 진행중 상태에서만 완료될 수 있습니다.");
    }
    this.status = BackupStatus.COMPLETED;
    this.endedAt = Instant.now();
    this.file = completedFile;
  }

  public void fail(File errorLogFile) {
    if (this.status != BackupStatus.IN_PROGRESS) {
       throw new IllegalStateException("백업 이력은 진행중 상태에서만 실패될 수 있습니다.");
    }
    this.status = BackupStatus.FAILED;
    this.endedAt = Instant.now();
    this.file = errorLogFile;
  }

  public void skip() {
    this.status = BackupStatus.SKIPPED;
    this.endedAt = Instant.now();
    this.file = null;
  }

  public void updateWorker(String worker) {
    this.worker = worker;
  }

  public void updateStartedAt(Instant startedAt) {
    this.startedAt = startedAt;
  }

  public void updateEndedAt(Instant endedAt) {
    this.endedAt = endedAt;
  }

  public void updateStatus(BackupStatus status) {
    this.status = status;
  }
}
