package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.domain.vo.authentication.RefreshTokenId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RefreshTokenJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenPersistenceMapper {

  public RefreshToken toDomain(RefreshTokenJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return RefreshToken.rehydrate(
        new RefreshTokenId(entity.getId()),
        new UserId(entity.getUserId()),
        entity.getTokenHash(),
        entity.getExpiresAt(),
        entity.getRevokedAt());
  }

  public RefreshTokenJpaEntity toEntity(RefreshToken token) {
    if (token == null) {
      return null;
    }
    RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
    entity.setId(token.getId().value());
    entity.setUserId(token.getUserId().value());
    entity.setTokenHash(token.getTokenHash());
    entity.setExpiresAt(token.getExpiresAt());
    entity.setRevokedAt(token.getRevokedAt());

    return entity;
  }

  public void updateEntityFromDomain(
      RefreshToken refreshToken, RefreshTokenJpaEntity existingEntity) {
    existingEntity.setUserId(refreshToken.getUserId().value());
    existingEntity.setTokenHash(refreshToken.getTokenHash());
    existingEntity.setExpiresAt(refreshToken.getExpiresAt());
    existingEntity.setRevokedAt(refreshToken.getRevokedAt());
  }
}
