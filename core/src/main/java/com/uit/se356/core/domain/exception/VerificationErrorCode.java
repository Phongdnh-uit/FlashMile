package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum VerificationErrorCode implements ErrorCode {
  INVALID_VERIFICATION_ID("VERIFICATION_001", "error.verification.invalid_id", 400),
  INVALID_EXPIRES_AT("VERIFICATION_002", "error.verification.invalid_expires_at", 400);

  private final String code;
  private final String messageKey;
  private final int httpStatus;

  VerificationErrorCode(String code, String messageKey, int httpStatus) {
    this.code = code;
    this.messageKey = messageKey;
    this.httpStatus = httpStatus;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessageKey() {
    return messageKey;
  }

  @Override
  public int getHttpStatus() {
    return httpStatus;
  }
}
