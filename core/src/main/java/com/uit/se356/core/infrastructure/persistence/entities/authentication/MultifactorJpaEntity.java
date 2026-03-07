package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.common.entity.BaseEntity;
import com.uit.se356.core.domain.vo.authentication.MultifactorMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "multifactors")
public class MultifactorJpaEntity extends BaseEntity<String> {

  @Column(nullable = false)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private MultifactorMethod method;

  @Column(nullable = false)
  private boolean isVerified = false;

  @Column(columnDefinition = "TEXT")
  private String details;
}
