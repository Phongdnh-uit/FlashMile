package com.uit.se356.core.application.seeding;

import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.port.RoleRepository;
import com.uit.se356.core.domain.constants.RoleName;
import com.uit.se356.core.domain.entities.authentication.Role;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeedingService {

  private final RoleRepository roleRepository;
  private final IdGenerator idGenerator;

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
  }
}
