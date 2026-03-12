package com.uit.se356.core.application.authentication.command.mfa;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.mfa.CompleteSetupMfaResult;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CompleteSetupMfaCommand(
    MfaMethod method,
    String credential, // Dùng cho TOTP và email
    Map<String, String> properties // Dùng cho webauth
    ) implements Command<CompleteSetupMfaResult> {
  public CompleteSetupMfaCommand {
    List<FieldError> errors = new ArrayList<>();
    if (method == null) {
      errors.add(
          new FieldError(
              "method", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"method"}));
    }
    // Credential có thể null nếu method là webauthn
    if (method != MfaMethod.WEBAUTHN
        && (credential == null || credential.isBlank())) {
      errors.add(
          new FieldError(
              "credential",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"credential"}));
    }

    if (method == MfaMethod.WEBAUTHN) {
        // Validation for WebAuthn properties is handled by the WebAuthnProvider
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
