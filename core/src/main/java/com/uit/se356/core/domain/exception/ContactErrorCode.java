package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum ContactErrorCode implements ErrorCode {
  CONTACT_NOT_FOUND("CONTACT_001", "error.contact.not_found", 404),
  DUPLICATE_PHONE_NUMBER("CONTACT_002", "error.contact.duplicate_phone", 400), // MSG2
  INVALID_MANDATORY_FIELDS(
      "CONTACT_003", "error.contact.mandatory_fields", 400); // Tương ứng MSG1/MSG2 missing field

  private final String code;
  private final String messageKey;
  private final int httpStatus;

  ContactErrorCode(String code, String messageKey, int httpStatus) {
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
