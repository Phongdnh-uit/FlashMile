package com.uit.se356.core.application.authentication.port.out;

import com.uit.se356.core.domain.entities.authentication.Permission;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import java.util.List;

public interface PermissionRepository {
  Permission create(Permission newPermission);

  Permission update(Permission permissionToUpdate);

  boolean existsByCode(String code);

  void deleteAll();

  List<Permission> findAllByRoleId(RoleId roleId);
}
