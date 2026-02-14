package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.LinkedAccountId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Objects;

public class LinkedAccount {
  private final LinkedAccountId id;
  private UserId userId;
  private String provider;
  private String providerUserId;

  private LinkedAccount(LinkedAccountId id, UserId userId, String provider, String providerUserId) {
    this.id = id;
    this.userId = userId;
    this.provider = provider;
    this.providerUserId = providerUserId;
  }

  // ============================ FACTORY ============================
  public static LinkedAccount create(
      LinkedAccountId id, UserId userId, String provider, String providerUserId) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(userId);
    Objects.requireNonNull(provider);
    Objects.requireNonNull(providerUserId);
    return new LinkedAccount(id, userId, provider, providerUserId);
  }

  public static LinkedAccount rehydrate(
      LinkedAccountId id, UserId userId, String provider, String providerUserId) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(userId);
    Objects.requireNonNull(provider);
    Objects.requireNonNull(providerUserId);
    return new LinkedAccount(id, userId, provider, providerUserId);
  }

  // ============================ GETTERS ============================
  public LinkedAccountId getId() {
    return id;
  }

  public UserId getUserId() {
    return userId;
  }

  public String getProvider() {
    return provider;
  }

  public String getProviderUserId() {
    return providerUserId;
  }
}
