package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleJpaEntity extends BaseEntity<String> {

  @Column(unique = true, nullable = false)
  private String name;

  private String description;

  @Column(nullable = false)
  private boolean isDefault = false;

  @Column(nullable = false)
  private boolean systemRole = false;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "role_permissions",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private Set<PermissionJpaEntity> permissions = new HashSet<>();
}
