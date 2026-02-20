package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import java.util.Optional;

public interface RoleRepository {

  Role create(Role newRole);

  Role update(Role roleToUpdate);

  Optional<Role> findById(RoleId roleId);

  Optional<Role> findDefault();

  boolean existsByName(String name);

  void delete(Role role);
}
