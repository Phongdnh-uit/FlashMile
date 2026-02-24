package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum UserErrorCode implements ErrorCode {
  INVALID_USER_ID("USER_001", "error.user.id.invalid", 400),
  INVALID_EMAIL_FORMAT("USER_002", "error.user.email.format", 400),
  INVALID_PHONE_FORMAT("USER_003", "error.user.phone.format", 400),
  INVALID_FULLNAME("USER_004", "error.user.fullname.invalid", 400),
  USER_NOT_FOUND("USER_005", "error.user.not_found", 404),
  USER_ALREADY_EXISTS("USER_006", "error.user.exists", 409),
  INVALID_USER_STATUS_TRANSITION("USER_007", "error.user.status.transition.invalid", 400),
  CANNOT_ACTIVATE_UNVERIFIED_USER("USER_008", "error.user.cannot_activate_unverified", 400);

  private final String code;
  private final String messageKey;
  private final int httpStatus;

  UserErrorCode(String code, String messageKey, int httpStatus) {
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
