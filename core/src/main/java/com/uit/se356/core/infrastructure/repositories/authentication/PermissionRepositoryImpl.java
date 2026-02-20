package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.PermissionRepository;
import com.uit.se356.core.domain.entities.authentication.Permission;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.PermissionJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.PermissionPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.PermissionJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class PermissionRepositoryImpl implements PermissionRepository {
  private final PermissionJpaRepository permissionJpaRepository;
  private final PermissionPersistenceMapper permissionPersistenceMapper;

  @Override
  @Transactional
  public Permission create(Permission newPermission) {
    PermissionJpaEntity entityToCreate = permissionPersistenceMapper.toEntity(newPermission);
    PermissionJpaEntity savedEntity = permissionJpaRepository.save(entityToCreate);
    return permissionPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public Permission update(Permission permissionToUpdate) {
    // 1. Lấy entity hiện tại
    PermissionJpaEntity existingEntity =
        permissionJpaRepository
            .findById(permissionToUpdate.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Permission not found with id: " + permissionToUpdate.getId().value()));
    // Hard-code exception do đã validate ở service layer rồi, nếu
    // không tìm thấy thì có thể throw exception hoặc handle
    // theo cách khác tùy nhu cầu

    // 2. Cập nhật entity với dữ liệu mới từ domain object
    permissionPersistenceMapper.updateFromDomain(permissionToUpdate, existingEntity);

    // 3. Không cần save vì @Transactional sẽ tự động flush các thay đổi khi transaction kết thúc
    return permissionPersistenceMapper.toDomain(existingEntity);
  }

  @Override
  public boolean existsByCode(String code) {
    return permissionJpaRepository.exists(
        (root, query, criteriaBuilder) -> {
          return criteriaBuilder.equal(root.get("code"), code);
        });
  }

  @Override
  @Transactional
  public void deleteAll() {
    permissionJpaRepository.deleteAll();
  }
}
