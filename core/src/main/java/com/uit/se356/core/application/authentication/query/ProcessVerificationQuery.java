package com.uit.se356.core.application.authentication.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.authentication.result.VerificationResult;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;

public record ProcessVerificationQuery(CodePurpose purpose, String recipient, String code)
    implements Query<VerificationResult> {
  public ProcessVerificationQuery {
    if (purpose == null || code == null || code.isBlank()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE_REQUEST);
    }
  }
}
