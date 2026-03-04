package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum AreaErrorCode implements ErrorCode {
  PROVINCE_NOT_FOUND("AREA_001", "error.area.province_not_found", 404),
  DUPLICATE_PROVINCE_CODE("AREA_002", "error.area.duplicate_province_code", 400),
  INVALID_BOUNDING_BOX("AREA_003", "error.area.invalid_bounding_box", 400);

  private final String code;
  private final String messageKey;
  private final int httpStatus;

  AreaErrorCode(String code, String messageKey, int httpStatus) {
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
