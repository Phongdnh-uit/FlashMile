package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import java.util.Optional;

public interface RefreshTokenRepository {
  RefreshToken save(RefreshToken refreshToken);

  Optional<RefreshToken> findByToken(String token);
}
