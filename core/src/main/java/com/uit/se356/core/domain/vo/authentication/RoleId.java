package com.uit.se356.core.domain.vo.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;

public record RoleId(String value) {
  public RoleId {
    if (value == null || value.isBlank()) {
      throw new AppException(CommonErrorCode.INVALID_ID_FORMAT);
    }
  }
}
