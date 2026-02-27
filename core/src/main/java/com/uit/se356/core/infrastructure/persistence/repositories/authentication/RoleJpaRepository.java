package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RoleJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends CommonRepository<RoleJpaEntity, String> {
  <T> Page<T> findBy(Specification<RoleJpaEntity> spec, Pageable pageable, Class<T> type);
}
