package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RefreshTokenJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.RefreshTokenPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.RefreshTokenJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
  private final RefreshTokenJpaRepository refreshTokenJpaRepository;
  private final RefreshTokenPersistenceMapper refreshTokenPersistenceMapper;

  @Override
  @Transactional
  public RefreshToken create(RefreshToken newRefreshToken) {
    RefreshTokenJpaEntity entityToCreate = refreshTokenPersistenceMapper.toEntity(newRefreshToken);
    RefreshTokenJpaEntity savedEntity = refreshTokenJpaRepository.save(entityToCreate);
    return refreshTokenPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public RefreshToken update(RefreshToken refreshTokenToUpdate) {
    // 1. Lấy lên entity hiện tại
    RefreshTokenJpaEntity existingEntity =
        refreshTokenJpaRepository
            .findById(refreshTokenToUpdate.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "RefreshToken not found with id: " + refreshTokenToUpdate.getId().value()));
    // Hard-code exception do đã validate ở service layer rồi, nếu
    // không tìm thấy thì có thể throw exception hoặc handle
    // theo cách khác tùy nhu cầu

    // 2. Cập nhật entity với dữ liệu mới từ domain object
    refreshTokenPersistenceMapper.updateEntityFromDomain(refreshTokenToUpdate, existingEntity);

    // 3. Không cần gọi save(), entity tự flush nếu hết transaction
    return refreshTokenPersistenceMapper.toDomain(existingEntity);
  }

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenJpaRepository
        .findOne((root, query, builder) -> builder.equal(root.get("tokenHash"), token))
        .map(refreshTokenPersistenceMapper::toDomain);
  }

  @Override
  public void cleanupExpiredTokens(long expirationInMillis) {
    Instant timeThreshold = Instant.now().minusMillis(expirationInMillis);
    refreshTokenJpaRepository.delete(
        (root, query, builder) ->
            builder.and(
                builder.isNotNull(root.get("expiresAt")),
                builder.lessThan(root.get("expiresAt"), (timeThreshold))));
  }
}
