package com.uit.se356.core.infrastructure.persistence.repositories.area;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.area.WardJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface WardJpaRepository extends CommonRepository<WardJpaEntity, String> {
  boolean existsByCode(String code);
}
