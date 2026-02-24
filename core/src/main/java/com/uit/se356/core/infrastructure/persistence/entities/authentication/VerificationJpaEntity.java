package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.common.entity.BaseEntity;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "verifications")
public class VerificationJpaEntity extends BaseEntity<String> {

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private VerificationType type;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private Instant expiresAt;
}
