package com.uit.se356.core.infrastructure.repositories;

import com.uit.se356.core.application.authentication.port.RoleRepository;
import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RoleJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.RolePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.RoleJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

  private final RoleJpaRepository roleJpaRepository;
  private final RolePersistenceMapper roleMapper;

  @Override
  public Role save(Role role) {
    // Kiểm tra xem là tạo mới hay cập nhật
    Optional<RoleJpaEntity> existingRoleOpt = roleJpaRepository.findById(role.getId().value());
    RoleJpaEntity entityToSave =
        existingRoleOpt
            .map(
                existingRole -> {
                  roleMapper.updateEntityFromDomain(role, existingRole);
                  return existingRole;
                })
            .orElseGet(() -> roleMapper.toEntity(role));
    RoleJpaEntity savedEntity = roleJpaRepository.save(entityToSave);
    return roleMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Role> findById(RoleId roleId) {
    return roleJpaRepository.findById(roleId.value()).map(roleMapper::toDomain);
  }

  @Override
  public Optional<Role> findDefault() {
    return roleJpaRepository
        .findOne((root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("isDefault")))
        .map(roleMapper::toDomain);
  }

  @Override
  public void delete(Role role) {
    roleJpaRepository.deleteById(role.getId().value());
  }

  @Override
  public boolean existsByName(String name) {
    return roleJpaRepository.exists(
        (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")), name.toLowerCase()));
  }
}
