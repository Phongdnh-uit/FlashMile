package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.core.domain.vo.authentication.UserStatus;
import com.uit.se356.core.infrastructure.persistence.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserJpaEntity extends BaseEntity {
  private String fullName;

  @Column(unique = true, nullable = false)
  private String phoneNumber;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String passwordHash;

  private boolean phoneVerified;
  private boolean emailVerified;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private UserStatus status;
}
