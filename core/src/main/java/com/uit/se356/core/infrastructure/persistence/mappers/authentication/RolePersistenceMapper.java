package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.AuditInfo;
import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RoleJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RolePersistenceMapper {

  public Role toDomain(RoleJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    AuditInfo auditInfo =
        AuditInfo.rehydrate(
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            new UserId(entity.getCreatedBy()),
            new UserId(entity.getUpdatedBy()));

    return Role.rehydrate(
        new RoleId(entity.getId()),
        entity.getName().name(),
        entity.getDescription(),
        entity.isDefault(),
        auditInfo);
  }

  public RoleJpaEntity toJpaEntity(Role domain) {
    if (domain == null) {
      return null;
    }

    RoleJpaEntity entity = new RoleJpaEntity();
    entity.setId(domain.getRoleId().value());
    entity.setName(com.uit.se356.core.domain.constants.RoleName.valueOf(domain.getName()));
    entity.setDescription(domain.getDescription());
    entity.setDefault(domain.isDefault());
    entity.setCreatedAt(domain.getAudit().getCreatedAt());
    entity.setCreatedBy(domain.getAudit().getCreatedBy().value());
    entity.setUpdatedAt(domain.getAudit().getUpdatedAt());
    entity.setUpdatedBy(domain.getAudit().getUpdatedBy().value());
    return entity;
  }
}
