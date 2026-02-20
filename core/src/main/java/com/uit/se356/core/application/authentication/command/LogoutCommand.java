package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AuthErrorCode;

public record LogoutCommand(String refreshToken) implements Command<Void> {
  public LogoutCommand {
    if (refreshToken == null || refreshToken.isBlank()) {
      throw new AppException(AuthErrorCode.INVALID_TOKEN);
    }
  }
}
