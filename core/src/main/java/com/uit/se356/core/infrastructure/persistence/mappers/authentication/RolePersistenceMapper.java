package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RoleJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RolePersistenceMapper {

  public Role toDomain(RoleJpaEntity entity) {
    if (entity == null) {
      return null;
    }

    return Role.rehydrate(
        new RoleId(entity.getId()), entity.getName(), entity.getDescription(), entity.isDefault());
  }

  public RoleJpaEntity toEntity(Role domain) {
    if (domain == null) {
      return null;
    }

    RoleJpaEntity entity = new RoleJpaEntity();
    entity.setId(domain.getId().value());
    entity.setName(domain.getName());
    entity.setDescription(domain.getDescription());
    entity.setDefault(domain.isDefault());
    return entity;
  }

  public void updateEntityFromDomain(Role role, RoleJpaEntity existingRole) {
    existingRole.setName(role.getName());
    existingRole.setDescription(role.getDescription());
    existingRole.setDefault(role.isDefault());
  }
}
