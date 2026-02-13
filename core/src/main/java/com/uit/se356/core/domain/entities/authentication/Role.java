package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.entities.AuditInfo;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Objects;

public class Role {
  private final RoleId roleId;
  private String name;
  private String description;
  private boolean isDefault;
  private AuditInfo audit;

  // ============================ FACTORY ============================
  private Role(RoleId roleId, String name, String description, boolean isDefault, AuditInfo audit) {
    this.roleId = roleId;
    this.name = name;
    this.description = description;
    this.isDefault = isDefault;
    this.audit = audit;
  }

  public static Role create(
      RoleId roleId, String name, String description, boolean isDefault, UserId by) {
    Objects.requireNonNull(roleId);
    Objects.requireNonNull(name);
    AuditInfo audit = AuditInfo.create(by);
    return new Role(roleId, name, description, isDefault, audit);
  }

  public static Role rehydrate(
      RoleId roleId, String name, String description, boolean isDefault, AuditInfo audit) {
    Objects.requireNonNull(roleId);
    Objects.requireNonNull(audit);
    return new Role(roleId, name, description, isDefault, audit);
  }

  // ============================ BEHAVIORS ============================
  public void update(String name, String description, boolean isDefault, UserId by) {
    Objects.requireNonNull(name);
    this.name = name;
    this.description = description;
    this.isDefault = isDefault;
    this.audit = this.audit.touched(by);
  }

  public void markAsDefault(UserId by) {
    this.isDefault = true;
    this.audit = this.audit.touched(by);
  }

  // ============================ GETTERS ============================
  public RoleId getRoleId() {
    return roleId;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isDefault() {
    return isDefault;
  }

  public AuditInfo getAudit() {
    return audit;
  }
}
