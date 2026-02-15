package com.uit.se356.core.infrastructure.repositories;

import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RefreshTokenJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.RefreshTokenPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.RefreshTokenJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
  private final RefreshTokenJpaRepository refreshTokenJpaRepository;
  private final RefreshTokenPersistenceMapper refreshTokenPersistenceMapper;

  @Override
  public RefreshToken save(RefreshToken refreshToken) {
    // Kiểm tra xem nó là trạng thái cập nhật hay update
    Optional<RefreshTokenJpaEntity> existingEntityOpt =
        refreshTokenJpaRepository.findById(refreshToken.getId().value());
    // Nếu tồn tại, cập nhật entity, nếu không, tạo mới
    RefreshTokenJpaEntity entityToSave =
        existingEntityOpt
            .map(
                existingEntity -> {
                  refreshTokenPersistenceMapper.updateEntityFromDomain(
                      refreshToken, existingEntity);
                  return existingEntity;
                })
            .orElseGet(() -> refreshTokenPersistenceMapper.toEntity(refreshToken));
    RefreshTokenJpaEntity savedEntity = refreshTokenJpaRepository.save(entityToSave);
    return refreshTokenPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenJpaRepository
        .findOne((root, query, builder) -> builder.equal(root.get("tokenHash"), token))
        .map(refreshTokenPersistenceMapper::toDomain);
  }
}
