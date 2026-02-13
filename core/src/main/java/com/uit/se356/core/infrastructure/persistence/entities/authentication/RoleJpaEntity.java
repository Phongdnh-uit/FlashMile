package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.core.domain.constants.RoleName;
import com.uit.se356.core.infrastructure.persistence.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleJpaEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(length = 20, unique = true, nullable = false)
  private RoleName name;

  private String description;

  @Column(nullable = false)
  private boolean isDefault = false;
}
