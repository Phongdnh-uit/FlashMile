package com.uit.se356.core.infrastructure.persistence.repositories.area;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.area.ProvinceJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceJpaRepository extends CommonRepository<ProvinceJpaEntity, String> {
  boolean existsByCode(String code);
}
