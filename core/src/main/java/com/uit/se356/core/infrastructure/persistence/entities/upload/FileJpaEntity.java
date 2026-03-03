package com.uit.se356.core.infrastructure.persistence.entities.upload;

import com.uit.se356.common.entity.BaseEntity;
import com.uit.se356.core.domain.vo.upload.FileStatus;
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
@Table(name = "files")
public class FileJpaEntity extends BaseEntity<String> {

  @Column(nullable = false, unique = true)
  private String storageKey;

  @Column(nullable = false)
  private String originalName;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Long size;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private FileStatus status;
}
