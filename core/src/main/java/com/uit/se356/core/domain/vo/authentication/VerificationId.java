package com.uit.se356.core.domain.vo.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.VerificationErrorCode;

public record VerificationId(String value) {
  public VerificationId {
    if (value == null || value.isBlank()) {
      throw new AppException(VerificationErrorCode.INVALID_VERIFICATION_ID);
    }
  }
}
