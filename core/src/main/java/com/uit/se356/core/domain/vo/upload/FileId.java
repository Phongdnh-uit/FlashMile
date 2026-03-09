package com.uit.se356.core.domain.vo.upload;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;

public record FileId(String value) {
  public FileId {
    if (value == null || value.isBlank()) {
      throw new AppException(CommonErrorCode.INVALID_ID_FORMAT);
    }
  }
}
