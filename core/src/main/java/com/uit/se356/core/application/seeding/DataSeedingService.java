package com.uit.se356.core.application.seeding;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.port.PasswordEncoder;
import com.uit.se356.core.application.authentication.port.RoleRepository;
import com.uit.se356.core.application.seeding.port.BootstrapConfigPort;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.constants.RoleName;
import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeedingService {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;
  private final IdGenerator idGenerator;
  private final BootstrapConfigPort bootstrapConfigPort;
  private final PasswordEncoder passwordEncoder;

  public void seedData() {
    seedDefaultRoles();
  }

  private void seedDefaultRoles() {
    // Seed vai trò nếu chưa tồn tại, sau đó gắn mặc định cho người dùng USER nếu chưa có
    boolean isDefaultRoleExists = roleRepository.findDefault().isPresent();
    for (RoleName roleName : RoleName.values()) {
      if (!roleRepository.existsByName(roleName.name())) {
        Role role =
            Role.create(new RoleId(idGenerator.generate().toString()), roleName.name(), "", false);
        if (!isDefaultRoleExists && roleName == RoleName.USER) {
          role.markAsDefault();
        }
        roleRepository.create(role);
      }
    }

    // Sau khi đã đảm bảo các role đã tồn tại, seed tài khoản admin nếu cần thiết
    seedDefaultAdmin();
  }

  private void seedDefaultAdmin() {
    // Seed tài khoản admin nếu chưa tồn tại
    // Dựa vào thông tin bootstrap để tạo tài khoản, các lần khởi tạo sau sẽ bỏ qua nếu đã tồn tại
    // một tài khoản có role admin

    // 1. Kiểm tra liệu role đã được seed chưa
    Role defaultAdminRole =
        roleRepository
            .findByName(RoleName.ADMIN.name())
            .orElseThrow(() -> new AppException(AuthErrorCode.ROLE_NOT_FOUND));

    List<User> adminUsers = userRepository.findByRoleId(defaultAdminRole.getId());

    if (adminUsers.isEmpty()) {
      // 2. Nếu chưa có admin nào, tạo tài khoản admin mới dựa trên thông tin bootstrap
      String fullName = bootstrapConfigPort.getAdminFullName();
      String email = bootstrapConfigPort.getAdminEmail();
      String password = bootstrapConfigPort.getAdminPassword();
      String phoneNumber = bootstrapConfigPort.getAdminPhoneNumber();

      String passwordHash = passwordEncoder.encode(password);
      User adminUser =
          User.create(
              new UserId(idGenerator.generate().toString()),
              fullName,
              new Email(email),
              passwordHash,
              new PhoneNumber(phoneNumber),
              defaultAdminRole.getId());
      adminUser.verifyEmail();
      adminUser.verifyPhone();
      adminUser.updateStatus(UserStatus.ACTIVE);

      userRepository.create(adminUser);
    }
  }
}
