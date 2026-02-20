package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {
  RefreshToken create(RefreshToken newRefreshToken);

  RefreshToken update(RefreshToken refreshTokenToUpdate);

  Optional<RefreshToken> findByToken(String token);

  /** Xóa tất cả refresh token đã hết hạn và quá expirationInMilis kể từ thời điểm hết hạn. */
  void cleanupExpiredTokens(long expirationInMillis);
}
