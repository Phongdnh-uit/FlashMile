package com.uit.se356.core.infrastructure.repositories.authentication;

import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.UserJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.UserPersistenceMapper;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.RoleJpaRepository;
import com.uit.se356.core.infrastructure.persistence.repositories.authentication.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {
  private final UserJpaRepository userJpaRepository;
  private final RoleJpaRepository roleJpaRepository;
  private final UserPersistenceMapper userPersistenceMapper;

  @Override
  @Transactional
  public User create(User newUser) {
    // Optional: Check if user already exists by email or phone, then throw exception.
    // For now, we proceed directly to saving.
    UserJpaEntity entityToCreate = userPersistenceMapper.toEntity(newUser);
    entityToCreate.setRole(roleJpaRepository.getReferenceById(newUser.getRoleId().value()));
    UserJpaEntity savedEntity = userJpaRepository.save(entityToCreate);
    return userPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  @Transactional
  public User update(User userToUpdate) {
    // 1. Lấy entity hiện tại
    UserJpaEntity existingEntity =
        userJpaRepository
            .findByIdWithRole(userToUpdate.getId().value())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "User not found with id: " + userToUpdate.getId().value()));
    // Hard-code exception do đã validate ở service layer rồi, nếu
    // không tìm thấy thì có thể throw exception hoặc handle
    // theo cách khác tùy nhu cầu

    // 2. Cập nhật các trường cần thiết từ domain object vào entity
    userPersistenceMapper.updateFromDomain(userToUpdate, existingEntity);
    // Gán lại tham chiều tới Role, mapper không xử lý được
    if (userToUpdate.getRoleId() != null) {
      existingEntity.setRole(roleJpaRepository.getReferenceById(userToUpdate.getRoleId().value()));
    }

    // 3. Không cần gọi save, entity tự động flush khi hết transaction
    return userPersistenceMapper.toDomain(existingEntity);
  }

  @Override
  public Optional<User> findById(UserId id) {
    return userJpaRepository.findByIdWithRole(id.value()).map(userPersistenceMapper::toDomain);
  }

  @Override
  public Optional<User> findByEmail(Email email) {
    return userJpaRepository.findByEmail(email.value()).map(userPersistenceMapper::toDomain);
  }

  @Override
  public Optional<User> findByPhoneNumber(PhoneNumber phoneNumber) {
    return userJpaRepository
        .findByPhoneNumber(phoneNumber.value())
        .map(userPersistenceMapper::toDomain);
  }

  @Override
  public List<User> findByStatus(UserStatus status) {
    return userJpaRepository.findByStatus(status).stream()
        .map(userPersistenceMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public List<User> findAll() {
    return userJpaRepository.findAllWithRole().stream()
        .map(userPersistenceMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
  public Page<User> findByStatus(UserStatus status, Pageable pageable) {
    return userJpaRepository.findByStatus(status, pageable).map(userPersistenceMapper::toDomain);
  }

  @Override
  public Page<User> findAll(Pageable pageable) {
    return userJpaRepository.findAllWithRole(pageable).map(userPersistenceMapper::toDomain);
  }

  @Override
  @Transactional
  public void delete(UserId id) {
    userJpaRepository.deleteById(id.value());
  }

  @Override
  public boolean existsByEmail(Email email) {
    return userJpaRepository.existsByEmail(email.value());
  }

  @Override
  public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
    return userJpaRepository.existsByPhoneNumber(phoneNumber.value());
  }

  @Override
  public boolean existsById(UserId id) {
    return userJpaRepository.existsById(id.value());
  }
}
