package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.domain.exception.AuthErrorCode;

public record TokenRotationCommand(String refreshToken) implements Command<LoginResult> {
  public TokenRotationCommand {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new AppException(AuthErrorCode.INVALID_TOKEN);
    }
  }
}
