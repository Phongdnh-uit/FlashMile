package com.uit.se356.core.application.authentication.command.role;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.RoleResult;
import java.util.HashMap;
import java.util.Map;

public record UpdateRoleCommand(String id, String name, String description, boolean isDefault)
    implements Command<RoleResult> {
  public UpdateRoleCommand {
    Map<String, Object> errors = new HashMap<>();
    if (id == null || id.isEmpty()) {
      errors.put("id", "Id is required");
    }
    if (name == null || name.isEmpty()) {
      errors.put("name", "Name is required");
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
