package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum ContactErrorCode implements ErrorCode {
  CONTACT_NOT_FOUND("CONTACT_001", "error.contact.not_found", 404),
  DUPLICATE_PHONE_NUMBER("CONTACT_002", "error.contact.duplicate_phone", 400);

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
