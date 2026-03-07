package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.MultifactorBackupCodeId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.time.Instant;
import java.util.Objects;

public class MultifactorBackupCode {

  private final MultifactorBackupCodeId id;
  private final UserId userId;
  private final String hashedCode;
  private Instant usedAt;

  private MultifactorBackupCode(
      MultifactorBackupCodeId id, UserId userId, String hashedCode, Instant usedAt) {
    this.id = Objects.requireNonNull(id);
    this.userId = Objects.requireNonNull(userId);
    this.hashedCode = Objects.requireNonNull(hashedCode);
    this.usedAt = usedAt;
  }

  // ============================ FACTORY ============================

  public static MultifactorBackupCode create(
      MultifactorBackupCodeId id, UserId userId, String hashedCode, Instant usedAt) {
    return new MultifactorBackupCode(id, userId, hashedCode, usedAt);
  }

  public static MultifactorBackupCode rehydrate(
      MultifactorBackupCodeId id, UserId userId, String hashedCode, Instant usedAt) {
    return new MultifactorBackupCode(id, userId, hashedCode, usedAt);
  }

  // ============================ BEHAVIOR ============================
  public void markAsUsed(Instant timestamp) {
    this.usedAt = Objects.requireNonNull(timestamp, "Timestamp cannot be null");
  }

  // ============================ GETTERS ============================
  public MultifactorBackupCodeId getId() {
    return id;
  }

  public UserId getUserId() {
    return userId;
  }

  public String getHashedCode() {
    return hashedCode;
  }

  public Instant getUsedAt() {
    return usedAt;
  }
}
