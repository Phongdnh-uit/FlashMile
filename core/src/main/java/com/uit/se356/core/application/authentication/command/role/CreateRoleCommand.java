package com.uit.se356.core.application.authentication.command.role;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.RoleResult;
import java.util.ArrayList;
import java.util.List;

public record CreateRoleCommand(String name, String description, boolean isDefault)
    implements Command<RoleResult> {
  public CreateRoleCommand {
    List<FieldError> errors = new ArrayList<>();
    if (name == null || name.isBlank()) {
      errors.add(
          new FieldError(
              "name", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"name"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
