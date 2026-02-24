package com.uit.se356.core.application.authentication.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.domain.exception.AuthErrorCode;

public record LoginQuery(String credentialId, String password) implements Query<LoginResult> {
  public LoginQuery {
    if (credentialId == null || credentialId.isBlank()) {
      throw new AppException(AuthErrorCode.CREDENTIAL_ID_INVALID);
    }
    if (password == null || password.isBlank()) {
      throw new AppException(AuthErrorCode.PASSWORD_INVALID);
    }
  }
}
