package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.MfaId;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.mfa.MfaConfig;
import java.util.Objects;

public class Mfa {

  private final MfaId id;
  private final UserId userId;
  private final MfaMethod method;
  private boolean isVerified;
  private MfaConfig config;

  private Mfa(MfaId id, UserId userId, MfaMethod method, boolean isVerified, MfaConfig config) {
    this.id = Objects.requireNonNull(id);
    this.userId = Objects.requireNonNull(userId);
    this.method = Objects.requireNonNull(method);
    this.isVerified = false;
    this.isVerified = isVerified;
    this.config = Objects.requireNonNull(config);
  }

  // ============================ FACTORY ============================
  public static Mfa create(MfaId id, UserId userId, MfaMethod method, MfaConfig config) {
    return new Mfa(id, userId, method, false, config);
  }

  public static Mfa rehydrate(
      MfaId id, UserId userId, MfaMethod method, MfaConfig config, boolean isVerified) {
    return new Mfa(id, userId, method, isVerified, config);
  }

  // ============================ BEHAVIOR ============================
  public void updateConfig(MfaConfig newConfig) {
    this.config = Objects.requireNonNull(newConfig);
  }

  public void markAsVerified() {
    this.isVerified = true;
  }

  // ============================ GETTERS ============================
  public MfaId getId() {
    return id;
  }

  public UserId getUserId() {
    return userId;
  }

  public MfaMethod getMethod() {
    return method;
  }

  public boolean isVerified() {
    return isVerified;
  }

  public MfaConfig getConfig() {
    return config;
  }
}
