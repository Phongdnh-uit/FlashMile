package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.common.entity.BaseEntity;
import jakarta.persistence.*;
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
}
