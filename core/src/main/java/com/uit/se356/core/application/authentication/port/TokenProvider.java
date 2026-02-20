package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.vo.authentication.UserId;

public interface TokenProvider {
  String generateToken(UserId userId);

  Long getTokenExpiryDuration();

  Long getRefreshTokenExpiryDuration();

  String generateSecureToken();

  String hashToken(String token);
}
