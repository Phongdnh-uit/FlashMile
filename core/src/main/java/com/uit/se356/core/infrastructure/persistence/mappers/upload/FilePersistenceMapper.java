package com.uit.se356.core.infrastructure.persistence.mappers.upload;

import com.uit.se356.core.domain.entities.upload.File;
import com.uit.se356.core.domain.vo.upload.FileId;
import com.uit.se356.core.infrastructure.persistence.entities.upload.FileJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FilePersistenceMapper {

  public File toDomain(FileJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return File.rehydrate(
        new FileId(entity.getId()),
        entity.getStorageKey(),
        entity.getOriginalName(),
        entity.getContentType(),
        entity.getSize(),
        entity.getStatus());
  }

  public FileJpaEntity toEntity(File domain) {
    if (domain == null) {
      return null;
    }

    FileJpaEntity entity = new FileJpaEntity();
    entity.setId(domain.getId().value());
    entity.setStorageKey(domain.getStorageKey());
    entity.setOriginalName(domain.getOriginalName());
    entity.setContentType(domain.getContentType());
    entity.setSize(domain.getSize());
    entity.setStatus(domain.getStatus());
    return entity;
  }

  public void updateEntityFromDomain(File domain, FileJpaEntity entity) {
    entity.setStorageKey(domain.getStorageKey());
    entity.setOriginalName(domain.getOriginalName());
    entity.setContentType(domain.getContentType());
    entity.setSize(domain.getSize());
    entity.setStatus(domain.getStatus());
  }
}
