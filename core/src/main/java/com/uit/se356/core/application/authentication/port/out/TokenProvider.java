package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.domain.vo.authentication.UserId;

public interface TokenProvider {
  String generateToken(UserId userId, RoleId roleId);

  Long getTokenExpiryDuration();

  Long getRefreshTokenExpiryDuration();

  String generateSecureToken();

  String hashToken(String token);
}
