package com.uit.se356.core.infrastructure.persistence.repositories.authentication;

import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.UserJpaEntity;
import com.uit.se356.core.infrastructure.persistence.mappers.authentication.UserPersistenceMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
  private final UserJpaRepository userJpaRepository;
  private final RoleJpaRepository roleJpaRepository;
  private final UserPersistenceMapper userPersistenceMapper;

  @Override
  public User save(User user) {
    // Kiểm tra xem là cập nhật hay tạo mới
    Optional<UserJpaEntity> userOpt = userJpaRepository.findById(user.getId().value());
    UserJpaEntity entityToSave =
        userOpt
            .map(
                existing -> {
                  userPersistenceMapper.updateFromDomain(user, existing);
                  return existing;
                })
            .orElseGet(() -> userPersistenceMapper.toEntity(user));
    // Gắn lại tham chiếu Role
    entityToSave.setRole(roleJpaRepository.getReferenceById(user.getRoleId().value()));
    UserJpaEntity savedEntity = userJpaRepository.save(entityToSave);
    return userPersistenceMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<User> findById(UserId id) {
    return userJpaRepository.findById(id.value()).map(userPersistenceMapper::toDomain);
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
    return userJpaRepository.findAll().stream()
        .map(userPersistenceMapper::toDomain)
        .collect(Collectors.toList());
  }

  @Override
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
