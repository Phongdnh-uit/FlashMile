package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.authentication.port.RoleRepository;
import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.RoleJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.RolePersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.RoleJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleRepositoryImpl implements RoleRepository {

  private final RoleJpaRepository roleJpaRepository;
  private final RolePersistenceMapper roleMapper;

  @Override
  @Transactional
  public Role create(Role newRole) {
    RoleJpaEntity entityToCreate = roleMapper.toEntity(newRole);
    RoleJpaEntity savedEntity = roleJpaRepository.save(entityToCreate);
    return roleMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public Role update(Role roleToUpdate) {
    // 1. Lấy entity hiện tại
    RoleJpaEntity existingEntity =
        roleJpaRepository
            .findById(roleToUpdate.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Role not found with id: " + roleToUpdate.getId().value()));
    // Hard-code exception do đã validate ở service layer rồi, nếu
    // không tìm thấy thì có thể throw exception hoặc handle
    // theo cách khác tùy nhu cầu

    // 2. Cập nhật entity với dữ liệu mới từ domain object
    roleMapper.updateEntityFromDomain(roleToUpdate, existingEntity);

    // 3. Không cần gọi save, entity tự flush khi hết transaction
    return roleMapper.toDomain(existingEntity);
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
  public Optional<Role> findByName(String name) {
    return roleJpaRepository
        .findOne(
            (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("name")), name.toLowerCase()))
        .map(roleMapper::toDomain);
  }

  @Override
  @Transactional
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
