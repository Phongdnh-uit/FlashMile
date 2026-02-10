package com.uit.se356.core.domain.entities;

import com.uit.se356.core.domain.vo.authentication.UserId;
import java.time.Instant;
import java.util.Objects;

public final class AuditInfo {

  private final Instant createdAt;
  private final Instant updatedAt;
  private final UserId createdBy;
  private final UserId updatedBy;

  private AuditInfo(Instant createdAt, Instant updatedAt, UserId createdBy, UserId updatedBy) {

    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
  }

  public static AuditInfo created(UserId by, Instant at) {
    Objects.requireNonNull(by);
    Objects.requireNonNull(at);
    return new AuditInfo(at, at, by, by);
  }

  public static AuditInfo rehydrate(
      Instant createdAt, Instant updatedAt, UserId createdBy, UserId updatedBy) {
    Objects.requireNonNull(createdAt);
    Objects.requireNonNull(updatedAt);
    Objects.requireNonNull(createdBy);
    Objects.requireNonNull(updatedBy);
    return new AuditInfo(createdAt, updatedAt, createdBy, updatedBy);
  }

  public AuditInfo touched(UserId by, Instant at) {
    Objects.requireNonNull(by);
    Objects.requireNonNull(at);
    return new AuditInfo(this.createdAt, at, this.createdBy, by);
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public UserId getCreatedBy() {
    return createdBy;
  }

  public UserId getUpdatedBy() {
    return updatedBy;
  }
}
