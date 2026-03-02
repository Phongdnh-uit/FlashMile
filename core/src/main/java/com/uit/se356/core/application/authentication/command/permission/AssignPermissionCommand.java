package com.uit.se356.core.application.authentication.command.permission;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.domain.vo.authentication.PermissionId;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public record AssignPermissionCommand(RoleId roleId, Set<PermissionId> permissionIds)
    implements Command<Void> {
  public AssignPermissionCommand {
    List<FieldError> errors = new ArrayList<>();
    if (roleId == null) {
      errors.add(new FieldError("roleId", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"roleId"}));
    }
  }
}
