package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.Multifactor;
import com.uit.se356.core.domain.vo.authentication.MultifactorId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.MultifactorJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MultifactorPersistenceMapper {

  public Multifactor toDomain(MultifactorJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return Multifactor.rehydrate(
        new MultifactorId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getMethod(),
        entity.getDetails(),
        entity.isVerified());
  }

  public MultifactorJpaEntity toEntity(Multifactor domain) {
    if (domain == null) {
      return null;
    }

    MultifactorJpaEntity entity = new MultifactorJpaEntity();
    entity.setId(domain.getId().value());
    entity.setUserId(domain.getUserId().value());
    entity.setMethod(domain.getMethod());
    entity.setDetails(domain.getDetails());
    entity.setVerified(domain.isVerified());
    return entity;
  }

  public void updateEntityFromDomain(Multifactor domain, MultifactorJpaEntity existingEntity) {
    existingEntity.setMethod(domain.getMethod());
    existingEntity.setDetails(domain.getDetails());
    existingEntity.setVerified(domain.isVerified());
  }
}
