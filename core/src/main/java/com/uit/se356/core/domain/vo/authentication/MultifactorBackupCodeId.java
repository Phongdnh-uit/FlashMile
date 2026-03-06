package com.uit.se356.core.domain.vo.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;

public record MultifactorBackupCodeId(String value) {
  public MultifactorBackupCodeId {
    if (value == null || value.isBlank()) {
      throw new AppException(CommonErrorCode.INVALID_ID_FORMAT);
    }
  }
}
