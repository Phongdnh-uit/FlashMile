package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.MultifactorBackupCode;
import com.uit.se356.core.domain.vo.authentication.MultifactorBackupCodeId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorBackupCodeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MultifactorBackupCodePersistenceMapper {

  public MultifactorBackupCode toDomain(MultifactorBackupCodeJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return MultifactorBackupCode.rehydrate(
        new MultifactorBackupCodeId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getHashedCode(),
        entity.getUsedAt());
  }

  public MultifactorBackupCodeJpaEntity toEntity(MultifactorBackupCode domain) {
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
      MultifactorBackupCode domain, MultifactorBackupCodeJpaEntity existingEntity) {
    existingEntity.setHashedCode(domain.getHashedCode());
    existingEntity.setUsedAt(domain.getUsedAt());
  }
}
