package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.entities.authentication.Permission;

public interface PermissionRepository {
  Permission save(Permission permission);

  boolean existsByCode(String code);

  void deleteAll();
}
