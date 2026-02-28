package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.RegisterResult;
import java.util.ArrayList;
import java.util.List;

public record RegisterCommand(
    String verificationToken, String email, String fullName, String password)
    implements Command<RegisterResult> {
  public RegisterCommand {
    List<FieldError> errors = new ArrayList<>();
    if (verificationToken == null || verificationToken.isBlank()) {
      errors.add(
          new FieldError(
              "verificationToken",
              "validation.field.required",
              new Object[] {"verification token"}));
    }
    if (email == null || email.isBlank()) {
      errors.add(new FieldError("email", "validation.field.required", new Object[] {"email"}));
    }
    if (fullName == null || fullName.isBlank()) {
      errors.add(
          new FieldError("fullName", "validation.field.required", new Object[] {"full name"}));
    }
    if (password == null || password.isBlank()) {
      errors.add(
          new FieldError("password", "validation.field.required", new Object[] {"password"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
