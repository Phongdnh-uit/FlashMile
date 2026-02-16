package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.VerificationJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationJpaRepository
    extends CommonRepository<VerificationJpaEntity, String> {}
