package com.uit.se356.core.application.authentication.command.mfa;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.authentication.result.LoginResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record VerifyMfaCommand(String challengeId, String code, Map<String, String> properties) implements Command<LoginResult> {
  public VerifyMfaCommand {
    List<FieldError> errors = new ArrayList<>();
    if (challengeId == null || challengeId.isBlank()) {
      errors.add(
          new FieldError(
              "challengeId",
              CommonErrorCode.FIELD_REQUIRED.getMessageKey(),
              new Object[] {"challengeId"}));
    }
    // Code có thể null nếu method là webauthn
    if (code == null || code.isBlank()) {
      errors.add(
          new FieldError(
              "code", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"code"}));
    }

    // TODO: Add validation for webauthn properties if method is webauthn

    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }

  public byte[] getCredentialId() {
    return properties != null ? properties.get("credentialId").getBytes() : null;
  }

  public byte[] getAuthenticatorData() {
    return properties != null ? properties.get("authenticatorData").getBytes() : null;
  }

  public byte[] getClientDataJSON() {
    return properties != null ? properties.get("clientDataJSON").getBytes() : null;
  }

  public byte[] getSignature() {
    return properties != null ? properties.get("signature").getBytes() : null;
  }
}
