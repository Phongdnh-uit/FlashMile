package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.VerificationErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.VerificationId;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import java.time.Instant;
import java.util.Objects;

public class Verification {
  private final VerificationId id;
  private UserId userId;
  private VerificationType type;
  private String code;
  private Instant expiresAt;

  // ============================ FACTORY ============================
  private Verification(
      VerificationId id, UserId userId, VerificationType type, String code, Instant expiresAt) {
    this.id = id;
    this.userId = userId;
    this.type = type;
    this.code = code;
    this.expiresAt = expiresAt;
  }

  public static Verification create(
      VerificationId id,
      UserId userId,
      VerificationType type,
      String code,
      Instant expiresAt,
      UserId by) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(userId);
    Objects.requireNonNull(type);
    Objects.requireNonNull(code);
    Objects.requireNonNull(expiresAt);
    if (expiresAt.isBefore(Instant.now())) {
      throw new AppException(VerificationErrorCode.INVALID_EXPIRES_AT);
    }
    return new Verification(id, userId, type, code, expiresAt);
  }

  public static Verification rehydrate(
      VerificationId id, UserId userId, VerificationType type, String code, Instant expiresAt) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(userId);
    Objects.requireNonNull(type);
    Objects.requireNonNull(code);
    return new Verification(id, userId, type, code, expiresAt);
  }

  // ============================ BEHAVIOR ============================
  public boolean isExpired() {
    return Instant.now().isAfter(expiresAt);
  }

  // ============================ GETTERS ============================
  public VerificationId getId() {
    return id;
  }

  public UserId getUserId() {
    return userId;
  }

  public VerificationType getType() {
    return type;
  }

  public String getCode() {
    return code;
  }

  public Instant getExpiresAt() {
    return expiresAt;
  }
}
