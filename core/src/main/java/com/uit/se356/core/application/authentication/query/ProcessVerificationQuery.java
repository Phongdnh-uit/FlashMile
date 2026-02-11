package com.uit.se356.core.application.authentication.query;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;

public record ProcessVerificationQuery(CodePurpose purpose, String recipient, String code) {
  public ProcessVerificationQuery {
    if (purpose == null
        || recipient == null
        || recipient.isBlank()
        || code == null
        || code.isBlank()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE_REQUEST);
    }
  }
}
