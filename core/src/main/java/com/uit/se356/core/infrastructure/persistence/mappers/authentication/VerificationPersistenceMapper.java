package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.AuditInfo;
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
    AuditInfo audit =
        AuditInfo.rehydrate(
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            new UserId(entity.getCreatedBy()),
            new UserId(entity.getUpdatedBy()));
    return Verification.rehydrate(
        new VerificationId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getType(),
        entity.getCode(),
        entity.getExpiresAt(),
        audit);
  }

  public VerificationJpaEntity toEntity(Verification verification) {
    if (verification == null) {
      return null;
    }
    VerificationJpaEntity entity = new VerificationJpaEntity();
    if (verification.getId() != null) {
      entity.setId(verification.getId().value());
    }
    if (verification.getUserId() != null) {
      entity.setUserId(verification.getUserId().value());
    }
    entity.setType(verification.getType());
    entity.setCode(verification.getCode());
    entity.setExpiresAt(verification.getExpiresAt());
    if (verification.getAudit() != null) {
      entity.setCreatedAt(verification.getAudit().getCreatedAt());
      entity.setUpdatedAt(verification.getAudit().getUpdatedAt());
      if (verification.getAudit().getCreatedBy() != null) {
        entity.setCreatedBy(verification.getAudit().getCreatedBy().value());
      }
      if (verification.getAudit().getUpdatedBy() != null) {
        entity.setUpdatedBy(verification.getAudit().getUpdatedBy().value());
      }
    }
    return entity;
  }
}
