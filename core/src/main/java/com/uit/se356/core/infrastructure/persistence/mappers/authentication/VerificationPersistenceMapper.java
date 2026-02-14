package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.Verification;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.VerificationId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.VerificationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VerificationPersistenceMapper {

  public Verification toDomain(VerificationJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return Verification.rehydrate(
        new VerificationId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getType(),
        entity.getCode(),
        entity.getExpiresAt());
  }

  public VerificationJpaEntity toEntity(Verification verification) {
    if (verification == null) {
      return null;
    }
    VerificationJpaEntity entity = new VerificationJpaEntity();
    entity.setId(verification.getId().value());
    entity.setUserId(verification.getUserId().value());
    entity.setType(verification.getType());
    entity.setCode(verification.getCode());
    entity.setExpiresAt(verification.getExpiresAt());
    return entity;
  }

  public void updateEntityFromDomain(Verification verification, VerificationJpaEntity existing) {
    if (verification == null || existing == null) {
      return;
    }
    existing.setUserId(verification.getUserId().value());
    existing.setType(verification.getType());
    existing.setCode(verification.getCode());
    existing.setExpiresAt(verification.getExpiresAt());
  }
}
