package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {
  INVALID_CREDENTIALS("AUTH_001", "error.auth.invalid_credentials", 401),
  CREDENTIAL_ID_INVALID("AUTH_002", "error.auth.credential_id_invalid", 400),
  PASSWORD_INVALID("AUTH_003", "error.auth.password_invalid", 400),
  TOKEN_GENERATION_FAILED("AUTH_004", "error.auth.token_generation_failed", 500),
  UNCATEGORIZED_EXCEPTION("AUTH_005", "error.auth.uncategorized", 500),
  PHONE_ALREADY_REGISTERED("AUTH_006", "error.auth.phone_already_registered", 400),
  INVALID_VERIFICATION_CODE_REQUEST(
      "AUTH_007", "error.auth.invalid_verification_code_request", 400),
  INVALID_VERIFICATION_CODE("AUTH_008", "error.auth.invalid_verification_code", 400),
  INVALID_REGISTER_COMMAND("AUTH_009", "error.auth.invalid_register_command", 400),
  EMAIL_ALREADY_VERIFIED("AUTH_010", "error.auth.email_already_verified", 400),
  USER_UNVERIFIED("AUTH_011", "error.auth.user_unverified", 403),
  USER_BLOCKED("AUTH_012", "error.auth.user_blocked", 403),
  ROLE_NOT_FOUND("AUTH_013", "error.auth.role_not_found", 500),
  EMAIL_ALREADY_USED("AUTH_014", "error.auth.email_already_used", 400),
  OAUTH2_AUTHORIZATION_REQUEST_FAILED(
      "AUTH_015", "error.auth.oauth2_authorization_request_failed", 401);
  private final String code;
  private final String messageKey;
  private final int httpStatus;

  AuthErrorCode(String code, String messageKey, int httpStatus) {
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
