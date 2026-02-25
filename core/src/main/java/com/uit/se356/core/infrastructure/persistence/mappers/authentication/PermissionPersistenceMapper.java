package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.Permission;
import com.uit.se356.core.domain.vo.authentication.PermissionId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.PermissionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PermissionPersistenceMapper {
  public PermissionJpaEntity toEntity(Permission permission) {
    if (permission == null) {
      return null;
    }
    PermissionJpaEntity entity = new PermissionJpaEntity();
    entity.setId(permission.getId().value());
    entity.setCode(permission.getCode());
    entity.setDescription(permission.getDescription());
    return entity;
  }

  public Permission toDomain(PermissionJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    Permission permission =
        Permission.rehydrate(
            new PermissionId(entity.getId()), entity.getCode(), entity.getDescription());
    return permission;
  }

  public void updateFromDomain(Permission permission, PermissionJpaEntity entity) {
    if (permission == null || entity == null) {
      return;
    }
    entity.setCode(permission.getCode());
    entity.setDescription(permission.getDescription());
  }
}
