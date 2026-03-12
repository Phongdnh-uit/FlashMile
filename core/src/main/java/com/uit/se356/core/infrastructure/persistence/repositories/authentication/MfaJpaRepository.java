package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorJpaEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface MfaJpaRepository extends CommonRepository<MultifactorJpaEntity, String> {}
