package com.uit.se356.core.domain.vo.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.UserErrorCode;

public record UserId(String value) {
  public UserId {
    if (value == null || value.isBlank()) {
      throw new AppException(UserErrorCode.INVALID_USER_ID);
    }
  }
}
