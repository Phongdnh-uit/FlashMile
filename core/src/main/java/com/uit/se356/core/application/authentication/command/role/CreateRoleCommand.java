package com.uit.se356.core.application.authentication.command.role;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.RoleResult;
import java.util.HashMap;
import java.util.Map;

public record CreateRoleCommand(String name, String description, boolean isDefault)
    implements Command<RoleResult> {
  public CreateRoleCommand {
    Map<String, Object> errors = new HashMap<>();
    if (name == null || name.isBlank()) {
      errors.put("name", "Name is required");
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
