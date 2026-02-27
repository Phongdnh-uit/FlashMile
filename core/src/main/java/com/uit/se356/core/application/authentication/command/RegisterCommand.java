package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.authentication.result.RegisterResult;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import java.util.HashMap;
import java.util.Map;

public record RegisterCommand(
    String verificationToken, String email, String fullName, String password)
    implements Command<RegisterResult> {
  public RegisterCommand {
    Map<String, String> errors = new HashMap<>();
    if (verificationToken == null || verificationToken.isBlank()) {
      errors.put("verificationToken", "Verification token is required.");
    }
    if (email == null || email.isBlank()) {
      errors.put("email", "Email is required.");
    }
    if (fullName == null || fullName.isBlank()) {
      errors.put("fullName", "Full name is required.");
    }
    if (password == null || password.isBlank()) {
      errors.put("password", "Password is required.");
    }
    if (!errors.isEmpty()) {
      throw new AppException(AuthErrorCode.INVALID_REGISTER_COMMAND, errors);
    }
  }
}
