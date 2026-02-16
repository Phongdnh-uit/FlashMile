package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.RefreshTokenId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.time.Instant;
import java.util.Objects;

public class RefreshToken {
  private final RefreshTokenId id;
  private UserId userId;
  private String tokenHash;
  private Instant expiresAt;
  private Instant revokedAt;

  // ============================ FACTORY ============================

  private RefreshToken(
      RefreshTokenId id, UserId userId, String tokenHash, Instant expiresAt, Instant revokedAt) {
    this.id = id;
    this.userId = userId;
    this.tokenHash = tokenHash;
    this.expiresAt = expiresAt;
    this.revokedAt = revokedAt;
  }

  public static RefreshToken create(
      RefreshTokenId id, UserId userId, String tokenHash, Instant expiresAt) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(userId);
    Objects.requireNonNull(tokenHash);
    Objects.requireNonNull(expiresAt);
    return new RefreshToken(id, userId, tokenHash, expiresAt, null);
  }

  public static RefreshToken rehydrate(
      RefreshTokenId id, UserId userId, String tokenHash, Instant expiresAt, Instant revokedAt) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(userId);
    Objects.requireNonNull(tokenHash);
    Objects.requireNonNull(expiresAt);
    return new RefreshToken(id, userId, tokenHash, expiresAt, revokedAt);
  }

  // ============================ BEHAVIOR ============================
  public void revoke() {
    if (this.revokedAt != null) {
      return;
    }
    this.revokedAt = Instant.now();
  }

  public boolean isValid() {
    return this.revokedAt == null && Instant.now().isBefore(this.expiresAt);
  }

  // ============================ GETTERS ============================
  public RefreshTokenId getId() {
    return id;
  }

  public UserId getUserId() {
    return userId;
  }

  public String getTokenHash() {
    return tokenHash;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }

  public Instant getRevokedAt() {
    return revokedAt;
  }
}
