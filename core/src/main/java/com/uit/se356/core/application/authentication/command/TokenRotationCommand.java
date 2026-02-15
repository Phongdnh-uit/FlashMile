package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AuthErrorCode;

public record TokenRotationCommand(String refreshToken) {
  public TokenRotationCommand {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new AppException(AuthErrorCode.INVALID_TOKEN);
    }
  }
}
