package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.PermissionId;
import java.util.Objects;

public class Permission {
  private final PermissionId id;
  private String code;
  private String description;

  private Permission(PermissionId id, String code, String description) {
    this.id = id;
    this.code = code;
    this.description = description;
  }

  // ============================ FACTORY ============================
  public static Permission create(PermissionId id, String code, String description) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(code);
    return new Permission(id, code, description);
  }

  public static Permission rehydrate(PermissionId id, String code, String description) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(code);
    return new Permission(id, code, description);
  }

  // ============================ GETTERS ============================
  public PermissionId getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
