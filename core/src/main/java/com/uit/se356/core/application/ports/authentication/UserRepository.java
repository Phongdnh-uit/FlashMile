package com.uit.se356.core.application.ports.authentication;

import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
  User save(User user);

  Optional<User> findById(UserId id);

  Optional<User> findByEmail(Email email);

  Optional<User> findByPhoneNumber(PhoneNumber phoneNumber);

  List<User> findByStatus(UserStatus status);

  List<User> findAll();

  void delete(UserId id);

  boolean existsByEmail(Email email);

  boolean existsByPhoneNumber(PhoneNumber phoneNumber);

  boolean existsById(UserId id);
}
