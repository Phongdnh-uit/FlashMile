package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.UserJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends CommonRepository<UserJpaEntity, String> {
  Optional<UserJpaEntity> findByEmail(String email);

  Optional<UserJpaEntity> findByPhoneNumber(String phoneNumber);

  List<UserJpaEntity> findByStatus(UserStatus status);

  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);
}
