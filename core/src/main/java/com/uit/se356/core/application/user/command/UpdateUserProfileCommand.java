package com.uit.se356.core.application.user.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.user.result.UserProfileResult;
import com.uit.se356.core.domain.exception.UserErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.HashMap;
import java.util.Map;

public record UpdateUserProfileCommand(UserId userId, String fullName)
    implements Command<UserProfileResult> {
  public UpdateUserProfileCommand {
    Map<String, Object> errors = new HashMap<>();

    // BR: Check Mandatory Fields
    if (userId == null || userId.value().isBlank()) {
      throw new AppException(UserErrorCode.IDENTITY_NOT_FOUND);
    }
    if (fullName == null || fullName.isBlank()) {
      throw new AppException(UserErrorCode.REQUIRED_FULLNAME_MISSING, "fullName");
    }
  }
}
