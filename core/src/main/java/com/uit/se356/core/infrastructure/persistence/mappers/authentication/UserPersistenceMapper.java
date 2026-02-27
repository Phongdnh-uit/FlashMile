package com.uit.se356.core.infrastructure.persistence.mappers.authentication;

import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.RoleId;
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
        new RoleId(entity.getRole().getId()));
  }

  public UserJpaEntity toEntity(User user) {
    if (user == null) {
      return null;
    }
    UserJpaEntity entity = new UserJpaEntity();
    entity.setId(user.getId().value());
    entity.setFullName(user.getFullName());
    entity.setEmail(user.getEmail().value());
    entity.setPasswordHash(user.getPasswordHash());
    entity.setPhoneNumber(user.getPhoneNumber().value());
    entity.setStatus(user.getStatus());
    entity.setPhoneVerified(user.isPhoneVerified());
    entity.setEmailVerified(user.isEmailVerified());
    return entity;
  }

  public void updateFromDomain(User user, UserJpaEntity entity) {
    if (user == null || entity == null) {
      return;
    }
    entity.setFullName(user.getFullName());
    entity.setEmail(user.getEmail().value());
    entity.setPasswordHash(user.getPasswordHash());
    entity.setPhoneNumber(user.getPhoneNumber().value());
    entity.setStatus(user.getStatus());
    entity.setPhoneVerified(user.isPhoneVerified());
    entity.setEmailVerified(user.isEmailVerified());
  }
}
