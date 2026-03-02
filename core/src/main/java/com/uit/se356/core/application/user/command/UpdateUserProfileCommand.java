package com.uit.se356.core.application.user.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.user.result.UserProfileResult;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.ArrayList;
import java.util.List;

public record UpdateUserProfileCommand(UserId userId, String fullName)
    implements Command<UserProfileResult> {
  public UpdateUserProfileCommand {
    List<FieldError> errors = new ArrayList<>();
    // BR: Check Mandatory Fields
    if (userId == null || userId.value().isBlank()) {
      errors.add(
          new FieldError(
              "userId", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"userId"}));
    }
    if (fullName == null || fullName.isBlank()) {
      errors.add(
          new FieldError(
              "fullName",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"fullName"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
