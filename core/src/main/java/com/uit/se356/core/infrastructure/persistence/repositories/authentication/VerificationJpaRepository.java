package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.common.repository.CommonRepository;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.VerificationJpaEntity;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationJpaRepository extends CommonRepository<VerificationJpaEntity, String> {

  Optional<VerificationJpaEntity> findByCodeAndType(String code, VerificationType type);

  void deleteByUserIdAndType(String userId, VerificationType type);
}

