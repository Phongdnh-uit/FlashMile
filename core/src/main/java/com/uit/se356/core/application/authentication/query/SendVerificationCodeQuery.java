package com.uit.se356.core.application.authentication.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;

public record SendVerificationCodeQuery(
    CodePurpose purpose, VerificationChannel channel, String recipient) implements Query<Void> {
  public SendVerificationCodeQuery {
    if (purpose == null || recipient == null || recipient.isBlank()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE_REQUEST);
    }
  }
}
