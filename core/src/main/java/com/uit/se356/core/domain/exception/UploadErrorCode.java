package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum UploadErrorCode implements ErrorCode {
  ;
  private final String code;
  private final String messageKey;
  private final int httpStatus;

  UploadErrorCode(String code, String messageKey, int httpStatus) {
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
