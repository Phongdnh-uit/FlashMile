package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RefreshTokenJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenJpaRepository extends CommonRepository<RefreshTokenJpaEntity, String> {
  Optional<RefreshTokenJpaEntity> findByTokenHash(String tokenHash);
}
