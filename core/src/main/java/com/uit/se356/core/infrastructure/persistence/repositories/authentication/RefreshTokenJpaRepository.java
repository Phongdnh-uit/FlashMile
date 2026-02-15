package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RefreshTokenJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenJpaRepository
    extends CommonRepository<RefreshTokenJpaEntity, String> {}
