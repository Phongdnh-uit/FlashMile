package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.LinkedAccountJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkedAccountJpaRepository
    extends CommonRepository<LinkedAccountJpaEntity, String> {}
