package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.LinkedAccount;
import com.uit.se356.core.domain.vo.authentication.LinkedAccountId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.LinkedAccountJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class LinkedAccountPersistenceMapper {

  public LinkedAccount toDomain(LinkedAccountJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return LinkedAccount.rehydrate(
        new LinkedAccountId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getProvider(),
        entity.getProviderUserId());
  }

  public LinkedAccountJpaEntity toEntity(LinkedAccount linkedAccount) {
    if (linkedAccount == null) {
      return null;
    }
    LinkedAccountJpaEntity entity = new LinkedAccountJpaEntity();
    entity.setId(linkedAccount.getId().value());
    entity.setUserId(linkedAccount.getUserId().value());
    entity.setProvider(linkedAccount.getProvider());
    entity.setProviderUserId(linkedAccount.getProviderUserId());
    return entity;
  }

  public void updateEntityFromDomain(LinkedAccount linkedAccount, LinkedAccountJpaEntity existing) {
    existing.setUserId(linkedAccount.getUserId().value());
    existing.setProvider(linkedAccount.getProvider());
    existing.setProviderUserId(linkedAccount.getProviderUserId());
  }
}
