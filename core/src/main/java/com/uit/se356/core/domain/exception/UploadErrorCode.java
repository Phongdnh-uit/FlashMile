package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum UploadErrorCode implements ErrorCode {
  INVALID_FILE_TYPE("UPLOAD_001", "error.upload.invalid_file_type", 400),
  FILE_TOO_LARGE("UPLOAD_002", "error.upload.file_too_large", 400),
  INVALID_FILE("UPLOAD_003", "error.upload.invalid_file", 400);
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
