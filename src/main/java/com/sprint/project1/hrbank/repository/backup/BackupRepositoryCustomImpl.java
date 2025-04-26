package com.sprint.project1.hrbank.repository.backup;

import com.sprint.project1.hrbank.dto.backup.BackupPagingRequest;
import com.sprint.project1.hrbank.entity.backup.Backup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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

    if (request.worker() != null && !request.worker().isBlank()) {
      predicates.add(cb.like(backup.get("worker"), "%" + request.worker() + "%"));
    }
    if (request.startedFrom() != null && request.startedTo() != null) {
      predicates.add(cb.between(backup.get("startedAt"), request.startedFrom(), request.startedTo()));
    }
    if (request.status() != null) {
      predicates.add(cb.equal(backup.get("status"), request.status()));
    }
    if (request.cursor() != null) {
      predicates.add(cb.lessThan(backup.get("id"), request.cursor()));
    }

    query.where(cb.and(predicates.toArray(new Predicate[0])));

    if ("endedAt".equals(request.sortBy() != null ? request.sortBy().name() : null)) {
      query.orderBy(cb.desc(backup.get("endedAt")));
    } else {
      query.orderBy(cb.desc(backup.get("startedAt")));
    }

    TypedQuery<Backup> typedQuery = em.createQuery(query);
    typedQuery.setMaxResults(request.size());

    return typedQuery.getResultList();
  }
}
