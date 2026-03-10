package com.uit.se356.core.application.authentication.command.mfa;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.LoginResult;
import java.util.ArrayList;
import java.util.List;

public record RecoveryMfaCommand(String challengeId, String code) implements Command<LoginResult> {
  public RecoveryMfaCommand {
    List<FieldError> errors = new ArrayList<>();
    if (challengeId == null || challengeId.isBlank()) {
      errors.add(
          new FieldError(
              "challengeId",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"challengeId"}));
    }
    if (code == null || code.isBlank()) {
      errors.add(
          new FieldError(
              "code", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"code"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
