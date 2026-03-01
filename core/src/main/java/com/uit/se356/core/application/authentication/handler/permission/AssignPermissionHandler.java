package com.uit.se356.core.application.authentication.handler.permission;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.security.HasPermission;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.authentication.command.permission.AssignPermissionCommand;
import com.uit.se356.core.application.authentication.port.out.PermissionRepository;
import com.uit.se356.core.application.authentication.port.out.RoleRepository;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.PermissionId;
import java.util.Set;

public class AssignPermissionHandler implements CommandHandler<AssignPermissionCommand, Void> {
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;

  public AssignPermissionHandler(
      RoleRepository roleRepository, PermissionRepository permissionRepository) {
    this.roleRepository = roleRepository;
    this.permissionRepository = permissionRepository;
  }

  @HasPermission("role:assign_permission")
  @Override
  public Void handle(AssignPermissionCommand command) {
    // 1. Kiểm tra xem role có tồn tại không
    var role = roleRepository.findById(command.roleId());
    if (role.isEmpty()) {
      throw new AppException(AuthErrorCode.ROLE_NOT_FOUND);
    }
    if (role.get().isSystemRole()) {
      throw new AppException(AuthErrorCode.SYSTEM_ROLE_MODIFICATION);
    }

    // 2. Lấy lên các quyền hạn và gán vào role
    // Note: Tái sử dụng hàm lấy projection để tránh việc load các field không cần thiết, do chỉ cần
    // id để gán quyền hạn vào role
    Set<PermissionId> permissions = permissionRepository.findExistingIds(command.permissionIds());
    if (permissions.size() != command.permissionIds().size()) {
      throw new AppException(AuthErrorCode.PERMISSION_NOT_FOUND);
    }
    // 3. Gán quyền hạn vào role
    role.get().assignPermissions(permissions);

    // 4. Lưu lại role
    roleRepository.update(role.get());
    return null;
  }
}
