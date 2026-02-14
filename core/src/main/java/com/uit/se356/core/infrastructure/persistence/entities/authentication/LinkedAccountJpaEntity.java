package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "linked_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LinkedAccountJpaEntity extends BaseEntity<String> {

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "provider", nullable = false)
  private String provider;

  @Column(name = "provider_user_id", nullable = false)
  private String providerUserId;
}
