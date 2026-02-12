package com.uit.se356.core.application.authentication.port;

public interface TokenProvider {
  String generateToken(String userId, String email, String role);

  String generateRefreshToken(String userId);

  Long getTokenExpiryDuration();

  Long getRefreshTokenExpiryDuration();
}
