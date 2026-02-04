package com.uit.se356.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AppError implements ErrorCode {
  ;
  private final String code;
  private final String messageKey;
  private final int httpStatus;
}
