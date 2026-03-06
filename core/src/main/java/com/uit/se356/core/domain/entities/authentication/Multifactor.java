package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.MultifactorId;
import com.uit.se356.core.domain.vo.authentication.MultifactorMethod;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Objects;

public class Multifactor {

  private final MultifactorId id;
  private final UserId userId;
  private final MultifactorMethod method;
  private boolean isVerified;
  private String details;

  private Multifactor(
      MultifactorId id,
      UserId userId,
      MultifactorMethod method,
      String details,
      boolean isVerified) {
    this.id = Objects.requireNonNull(id);
    this.userId = Objects.requireNonNull(userId);
    this.method = Objects.requireNonNull(method);
    this.isVerified = false;
    this.details = details;
    this.isVerified = isVerified;
  }

  // ============================ FACTORY ============================
  public static Multifactor create(
      MultifactorId id, UserId userId, MultifactorMethod method, String details) {
    return new Multifactor(id, userId, method, details, false);
  }

  public static Multifactor rehydrate(
      MultifactorId id,
      UserId userId,
      MultifactorMethod method,
      String details,
      boolean isVerified) {
    return new Multifactor(id, userId, method, details, isVerified);
  }

  // ============================ BEHAVIOR ============================
  public void updateDetails(String details) {
    this.details = details;
  }

  public void markAsVerified() {
    this.isVerified = true;
  }

  // ============================ GETTERS ============================
  public MultifactorId getId() {
    return id;
  }

  public UserId getUserId() {
    return userId;
  }

  public MultifactorMethod getMethod() {
    return method;
  }

  public boolean isVerified() {
    return isVerified;
  }

  public String getDetails() {
    return details;
  }
}
