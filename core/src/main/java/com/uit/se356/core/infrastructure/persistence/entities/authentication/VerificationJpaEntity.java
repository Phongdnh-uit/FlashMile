package com.uit.se356.core.infrastructure.persistence.entities.authentication;
}
  private Instant expiresAt;
  @Column(nullable = false)

  private String code;
  @Column(nullable = false)

  private VerificationType type;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)

  private String userId;
  @Column(nullable = false)

public class VerificationJpaEntity extends BaseEntity {
@Table(name = "verifications")
@Entity
@Setter
@Getter

import lombok.Setter;
import lombok.Getter;
import java.time.Instant;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import com.uit.se356.core.infrastructure.persistence.entities.BaseEntity;
import com.uit.se356.core.domain.vo.authentication.VerificationType;


