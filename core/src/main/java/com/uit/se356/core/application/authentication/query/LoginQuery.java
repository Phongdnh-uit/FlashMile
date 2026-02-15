package com.uit.se356.core.application.authentication.query;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AuthErrorCode;

public record LoginQuery(String credentialId, String password) {
  public LoginQuery {
    if (credentialId == null || credentialId.isBlank()) {
      throw new AppException(AuthErrorCode.CREDENTIAL_ID_INVALID);
    }
    if (password == null || password.isBlank()) {
      throw new AppException(AuthErrorCode.PASSWORD_INVALID);
    }
  }
}
