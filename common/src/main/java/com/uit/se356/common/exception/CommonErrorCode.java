package com.uit.se356.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
  EMAIL_SENDING_FAILED("COMMON-0001", "error.common.email.sending_failed", 500),
  INVALID_ID_FORMAT("COMMON-0000", "error.common.invalid_id_format", 400),
  VALIDATION_ERROR("COMMON-0002", "error.common.validation_error", 400),
  INTERNAL_ERROR("COMMON-0003", "error.common.internal_error", 500);
  private final String code;
  private final String messageKey;
  private final int httpStatus;
}
