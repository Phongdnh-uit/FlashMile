package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.RoleId;
import java.util.Objects;

public class Role {
  private final RoleId id;
  private String name;
  private String description;
  private boolean isDefault;
  private boolean
      systemRole; // Flag để phân biệt role hệ thống (không thể xóa/sửa) và role người dùng

  // ============================ FACTORY ============================
  private Role(RoleId id, String name, String description, boolean isDefault, boolean systemRole) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.isDefault = isDefault;
    this.systemRole = systemRole;
  }

  public static Role create(RoleId id, String name, String description) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    return new Role(id, name, description, false, false);
  }

  public static Role rehydrate(
      RoleId id, String name, String description, boolean isDefault, boolean systemRole) {
    Objects.requireNonNull(id);
    return new Role(id, name, description, isDefault, systemRole);
  }

  public static Role createSystemRole(RoleId id, String name, String description) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    return new Role(id, name, description, false, true);
  }

  // ============================ BEHAVIORS ============================
  public void update(String name, String description, boolean isDefault) {
    Objects.requireNonNull(name);
    this.name = name;
    this.description = description;
    this.isDefault = isDefault;
  }

  public void markAsDefault() {
    this.isDefault = true;
  }

  // ============================ GETTERS ============================
  public RoleId getId() {
    return id;
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

  public boolean isSystemRole() {
    return systemRole;
  }
}
