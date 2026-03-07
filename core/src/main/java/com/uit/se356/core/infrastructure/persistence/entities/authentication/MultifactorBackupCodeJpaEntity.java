package com.uit.se356.core.infrastructure.persistence.entities.authentication;

import com.uit.se356.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "multifactor_backup_codes")
public class MultifactorBackupCodeJpaEntity extends BaseEntity<String> {

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false)
  private String hashedCode;

  private Instant usedAt;
}
