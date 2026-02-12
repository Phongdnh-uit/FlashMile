package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.core.infrastructure.persistence.entities.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenJpaEntity extends BaseEntity {

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private String tokenHash;

  @Column(nullable = false)
  private Instant expiresAt;

  private Instant revokedAt;
}
