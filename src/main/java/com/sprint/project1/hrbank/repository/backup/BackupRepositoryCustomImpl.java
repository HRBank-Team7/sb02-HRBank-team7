package com.sprint.project1.hrbank.repository.backup;

import com.sprint.project1.hrbank.dto.backup.BackupPagingRequest;
import com.sprint.project1.hrbank.dto.backup.BackupSortDirection;
import com.sprint.project1.hrbank.dto.backup.BackupSortField;
import com.sprint.project1.hrbank.entity.backup.Backup;
import com.sprint.project1.hrbank.util.CursorManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BackupRepositoryCustomImpl implements BackupRepositoryCustom {

  @PersistenceContext
  private final EntityManager em;

  @Override
  public List<Backup> search(BackupPagingRequest request) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Backup> query = cb.createQuery(Backup.class);
    Root<Backup> backup = query.from(Backup.class);

    backup.fetch("file", JoinType.LEFT);

    List<Predicate> predicates = new ArrayList<>();

    // worker 필터링
    if (request.worker() != null && !request.worker().isBlank()) {
      predicates.add(cb.like(backup.get("worker"), "%" + request.worker() + "%"));
    }
    // 시작일 범위 필터링
    if (request.startedAtFrom() != null && request.startedAtTo() != null) {
      predicates.add(cb.between(backup.get("startedAt"), request.startedAtFrom(), request.startedAtTo()));
    }
    // status 필터링
    if (request.status() != null) {
      predicates.add(cb.equal(backup.get("status"), request.status()));
    }

    Instant cursorStartedAt = CursorManager.decode(request.cursor());

    if (cursorStartedAt != null && request.idAfter() != null) {
      Predicate cursorPredicate;
      if (request.sortDirection() == BackupSortDirection.DESC) {
        cursorPredicate = cb.or(
            cb.lessThan(backup.get("startedAt"), cursorStartedAt),
            cb.and(
                cb.equal(backup.get("startedAt"), cursorStartedAt),
                cb.lessThan(backup.get("id"), request.idAfter())
            )
        );
      } else { // ASC
        cursorPredicate = cb.or(
            cb.greaterThan(backup.get("startedAt"), cursorStartedAt),
            cb.and(
                cb.equal(backup.get("startedAt"), cursorStartedAt),
                cb.greaterThan(backup.get("id"), request.idAfter())
            )
        );
      }
      predicates.add(cursorPredicate);
    }

    query.where(cb.and(predicates.toArray(new Predicate[0])));

    // 정렬 조건
    Order order = buildOrder(cb, backup, request.sortField(), request.sortDirection());
    query.orderBy(order);

    TypedQuery<Backup> typedQuery = em.createQuery(query);
    typedQuery.setMaxResults(request.size());

    return typedQuery.getResultList();
  }

  @Override
  public Long countByConditions(BackupPagingRequest request) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    Root<Backup> backup = query.from(Backup.class);

    List<Predicate> predicates = new ArrayList<>();

    if (request.worker() != null && !request.worker().isBlank()) {
      predicates.add(cb.like(backup.get("worker"), "%" + request.worker() + "%"));
    }
    if (request.status() != null) {
      predicates.add(cb.equal(backup.get("status"), request.status()));
    }
    if (request.startedAtFrom() != null && request.startedAtTo() != null) {
      predicates.add(cb.between(backup.get("startedAt"), request.startedAtFrom(), request.startedAtTo()));
    }

    query.select(cb.count(backup)).where(cb.and(predicates.toArray(new Predicate[0])));
    return em.createQuery(query).getSingleResult();
  }


  private Order buildOrder(CriteriaBuilder cb, Root<Backup> backup, BackupSortField sortField, BackupSortDirection sortDirection) {
    BackupSortField field = (sortField == null) ? BackupSortField.STARTED_AT : sortField;
    BackupSortDirection direction =
        (sortDirection == null) ? BackupSortDirection.DESC : sortDirection;

    Path<?> sortPath;
    switch (field) {
      case STARTED_AT -> sortPath = backup.get("startedAt");
      case ENDED_AT -> sortPath = backup.get("endedAt");
      default -> throw new IllegalArgumentException("지원하지 않는 정렬 필드입니다: " + field);
    }

    return (direction == BackupSortDirection.ASC)
        ? cb.asc(sortPath)
        : cb.desc(sortPath);
  }
}
