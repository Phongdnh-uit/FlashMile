package com.uit.se356.core.domain.exception;

import com.uit.se356.common.exception.ErrorCode;

public enum AreaErrorCode implements ErrorCode {
  PROVINCE_NOT_FOUND("AREA_001", "error.area.province_not_found", 404),
  DUPLICATE_PROVINCE_CODE("AREA_002", "error.area.duplicate_province_code", 400),
  INVALID_BOUNDING_BOX("AREA_003", "error.area.invalid_bounding_box", 400),
  INVALID_PROVINCE_ID("AREA_004", "error.area.invalid_province_id", 400),

  WARD_NOT_FOUND("AREA_005", "error.area.ward_not_found", 404),
  DUPLICATE_WARD_CODE("AREA_006", "error.area.duplicate_ward_code", 400),
  MISSING_PROVINCE_ID("AREA_007", "error.area.missing_province_id", 400),
  INVALID_WARD_ID("AREA_008", "error.area.invalid_ward_id", 400);

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
