package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.MfaBackupCode;
import com.uit.se356.core.domain.vo.authentication.MfaBackupCodeId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorBackupCodeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MfaBackupCodePersistenceMapper {

  public MfaBackupCode toDomain(MultifactorBackupCodeJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return MfaBackupCode.rehydrate(
        new MfaBackupCodeId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getHashedCode(),
        entity.getUsedAt());
  }

  public MultifactorBackupCodeJpaEntity toEntity(MfaBackupCode domain) {
    if (domain == null) {
      return null;
    }

    MultifactorBackupCodeJpaEntity entity = new MultifactorBackupCodeJpaEntity();
    entity.setId(domain.getId().value());
    entity.setUserId(domain.getUserId().value());
    entity.setHashedCode(domain.getHashedCode());
    entity.setUsedAt(domain.getUsedAt());
    return entity;
  }

  public void updateEntityFromDomain(
      MfaBackupCode domain, MultifactorBackupCodeJpaEntity existingEntity) {
    existingEntity.setHashedCode(domain.getHashedCode());
    existingEntity.setUsedAt(domain.getUsedAt());
  }
}
