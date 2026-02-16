package com.uit.se356.core.domain.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Objects;

public class Role {
  private final RoleId id;
  private String name;
  private String description;
  private boolean isDefault;

  // ============================ FACTORY ============================
  private Role(RoleId id, String name, String description, boolean isDefault) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.isDefault = isDefault;
  }

  public static Role create(
      RoleId id, String name, String description, boolean isDefault, UserId by) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    return new Role(id, name, description, isDefault);
  }

  public static Role rehydrate(RoleId id, String name, String description, boolean isDefault) {
    Objects.requireNonNull(id);
    return new Role(id, name, description, isDefault);
  }

  // ============================ BEHAVIORS ============================
  public void update(String name, String description, boolean isDefault, UserId by) {
    Objects.requireNonNull(name);
    this.name = name;
    this.description = description;
    this.isDefault = isDefault;
  }

  public void markAsDefault(UserId by) {
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
}
