package com.uit.se356.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommonErrorCode implements ErrorCode {
    USER_NOT_FOUND(
            "USER_NOT_FOUND",
            "error.user.not_found",
            404
    ),
    RESOURCE_ALREADY_EXISTS(
            "RESOURCE_ALREADY_EXISTS",
            "error.resource.already_exists",
            409
    ),
    RESOURCE_NOT_FOUND(
            "RESOURCE_NOT_FOUND",
            "error.resource.not_found",
            404
    );

  private final String code;
  private final String messageKey;
  private final int httpStatus;
}
