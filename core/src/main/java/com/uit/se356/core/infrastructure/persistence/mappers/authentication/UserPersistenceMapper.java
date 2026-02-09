package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.persistence.entities.authentication.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

  public User toDomain(UserJpaEntity entity) {
    if (entity == null) {
      return null;
    }
    return User.rehydrate(
        new UserId(entity.getId()),
        entity.getFullName(),
        new Email(entity.getEmail()),
        entity.getPasswordHash(),
        new PhoneNumber(entity.getPhoneNumber()),
        entity.getStatus(),
        entity.isPhoneVerified(),
        entity.isEmailVerified(),
        entity.getCreatedAt(),
        entity.getUpdatedAt(),
        entity.getCreatedBy() != null ? new UserId(entity.getCreatedBy()) : null,
        entity.getUpdatedBy() != null ? new UserId(entity.getUpdatedBy()) : null);
  }

  public UserJpaEntity toEntity(User user) {
    if (user == null) {
      return null;
    }
    UserJpaEntity entity = new UserJpaEntity();
    if (user.getUserId() != null) {
      entity.setId(user.getUserId().value());
    }
    entity.setFullName(user.getFullName());
    if (user.getEmail() != null) {
      entity.setEmail(user.getEmail().value());
    }
    entity.setPasswordHash(user.getPasswordHash());
    if (user.getPhoneNumber() != null) {
      entity.setPhoneNumber(user.getPhoneNumber().value());
    }
    entity.setStatus(user.getStatus());
    entity.setPhoneVerified(user.isPhoneVerified());
    entity.setEmailVerified(user.isEmailVerified());
    entity.setCreatedAt(user.getCreatedAt());
    entity.setUpdatedAt(user.getUpdatedAt());
    if (user.getCreatedBy() != null) {
      entity.setCreatedBy(user.getCreatedBy().value());
    }
    if (user.getUpdatedBy() != null) {
      entity.setUpdatedBy(user.getUpdatedBy().value());
    }
    return entity;
  }
}
