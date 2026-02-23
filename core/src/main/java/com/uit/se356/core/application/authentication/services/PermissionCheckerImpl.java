package com.uit.se356.core.application.authentication.services;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.application.authentication.port.AuthCacheRepository;
import com.uit.se356.core.application.authentication.port.PermissionRepository;
import com.uit.se356.core.application.authentication.port.RoleRepository;
import com.uit.se356.core.application.authentication.port.in.PermissionChecker;
import com.uit.se356.core.domain.constants.CacheKey;
import com.uit.se356.core.domain.constants.RoleName;
import com.uit.se356.core.domain.entities.authentication.Permission;
import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionCheckerImpl implements PermissionChecker {
  private final AuthCacheRepository cacheRepository;
  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;
  private final SecurityUtil<UserId> securityUtil;

  public PermissionCheckerImpl(
      AuthCacheRepository cacheRepository,
      PermissionRepository permissionRepository,
      SecurityUtil<UserId> securityUtil,
      RoleRepository roleRepository) {
    this.cacheRepository = cacheRepository;
    this.permissionRepository = permissionRepository;
    this.securityUtil = securityUtil;
    this.roleRepository = roleRepository;
  }

  // TODO: Cần tối ưu lại, hiện tại đang có khá nhiều bất cập và nhập nhằng giữa các repository, phụ
  // thuộc chéo giữa các jpa entity
  @Override
  public void checkCurrentUserHasPermission(String permission) {
    // Lấy vai trò hiện tại của người dùng từ UserPrincipal, role lưu trong UserPrincipal là roleId
    RoleId roleId =
        new RoleId(
            securityUtil
                .getCurrentUserPrincipal()
                .orElseThrow(() -> new AppException(AuthErrorCode.ACCESS_DENIED))
                .getRole());

    // Bypass nếu là quyền hạn ADMIN
    Optional<Role> adminRole = roleRepository.findById(roleId);
    if (adminRole.isPresent()
        && adminRole.get().isSystemRole()
        && RoleName.ADMIN.name().equals(adminRole.get().getName())) {
      return;
    }

    // Kiểm tra quyền hạn trong cache trước, phải tự deserialize vì cache lưu dưới dạng String
    String cacheKey = CacheKey.PERMISSION_LIST + ":" + roleId.value();
    Optional<Set<String>> permissionList = cacheRepository.getSet(cacheKey);

    // Nếu không có trong cache, truy vấn từ database và lưu vào cache lại
    if (permissionList.isEmpty()) {
      List<Permission> permissions = permissionRepository.findAllByRoleId(roleId);
      Set<String> permissionSet =
          permissions.stream().map(Permission::getCode).collect(Collectors.toSet());
      cacheRepository.setSet(cacheKey, permissionSet, Duration.ofHours(1));
      permissionList = Optional.of(permissionSet);
    }

    // Kiểm tra nếu permission không tồn tại trong danh sách quyền hạn của vai trò, ném lỗi
    if (permissionList.isPresent() && !permissionList.get().contains(permission)) {
      throw new AppException(AuthErrorCode.ACCESS_DENIED);
    }
  }
}
