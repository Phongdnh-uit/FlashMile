package com.uit.se356.core.application.authentication.command.mfa;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CompleteSetupMfaCommand(
    MfaMethod method,
    String credential, // Dùng cho TOTP và email
    Map<String, String> properties // Dùng cho webauth
    ) implements Command<Void> {
  public CompleteSetupMfaCommand {
    List<FieldError> errors = new ArrayList<>();
    if (method == null) {
      errors.add(
          new FieldError(
              "method", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"method"}));
    }
    // Credential có thể null nếu method là webauth, nhưng nếu method là TOTP hoặc email thì
    // credential không được null
    if ((method == MfaMethod.TOTP || method == MfaMethod.EMAIL)
        && (credential == null || credential.isBlank())) {
      errors.add(
          new FieldError(
              "credential",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"credential"}));
    }

    if (method == MfaMethod.WEBAUTHN) {
      // WebAuthn yêu cầu properties phải có "publicKey" và "attestationObject"
      if (properties == null
          || !properties.containsKey("publicKey")
          || !properties.containsKey("attestationObject")) {
        errors.add(
            new FieldError(
                "properties",
                CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
                new Object[] {"properties with publicKey and attestationObject"}));
      }
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
