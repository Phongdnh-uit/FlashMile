package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class PermissionJpaEntity extends BaseEntity<String> {
  @Column(nullable = false, unique = true)
  private String code;

  private String description;
}
