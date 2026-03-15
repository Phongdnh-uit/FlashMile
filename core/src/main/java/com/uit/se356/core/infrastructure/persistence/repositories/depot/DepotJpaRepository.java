package com.uit.se356.core.infrastructure.persistence.repositories.depot;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.depot.DepotJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface DepotJpaRepository extends CommonRepository<DepotJpaEntity, String> {}
