package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import java.util.HashMap;
import java.util.Map;

public record ResetPasswordCommand(String verificationToken, String newPassword) {
  public ResetPasswordCommand {
    Map<String, Object> errors = new HashMap<>();
    if (verificationToken == null || verificationToken.isBlank()) {
      errors.put("verificationToken", "Verification token must not be null or blank");
    }
    if (newPassword == null || newPassword.isBlank()) {
      errors.put("newPassword", "New password must not be null or blank");
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR);
    }
  }
}
