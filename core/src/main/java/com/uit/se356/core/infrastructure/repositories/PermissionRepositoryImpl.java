package com.uit.se356.core.infrastructure.repositories;

import com.uit.se356.core.application.authentication.port.PermissionRepository;
import com.uit.se356.core.domain.entities.authentication.Permission;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.PermissionJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.PermissionPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.PermissionJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PermissionRepositoryImpl implements PermissionRepository {
  private final PermissionJpaRepository permissionJpaRepository;
  private final PermissionPersistenceMapper permissionPersistenceMapper;

  @Override
  public Permission save(Permission permission) {
    // Kiểm tra là trạng thái mới hay cập nhật
    Optional<PermissionJpaEntity> existingPermissionOpt =
        permissionJpaRepository.findById(permission.getId().value());
    PermissionJpaEntity permissionJpaEntity =
        existingPermissionOpt
            .map(
                existing -> {
                  permissionPersistenceMapper.updateFromDomain(permission, existing);
                  return existing;
                })
            .orElseGet(() -> permissionPersistenceMapper.toEntity(permission));
    PermissionJpaEntity savedEntity = permissionJpaRepository.save(permissionJpaEntity);
    return permissionPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public boolean existsByCode(String code) {
    return permissionJpaRepository.exists(
        (root, query, criteriaBuilder) -> {
          return criteriaBuilder.equal(root.get("code"), code);
        });
  }

  @Override
  public void deleteAll() {
    permissionJpaRepository.deleteAll();
  }
}
