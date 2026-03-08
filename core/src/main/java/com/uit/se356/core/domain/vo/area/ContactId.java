package com.uit.se356.core.domain.vo.area;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.ContactErrorCode;

public record ContactId(String value) {
  public ContactId {
    if (value == null || value.isBlank()) {
      throw new AppException(ContactErrorCode.INVALID_CONTACT_ID);
    }
  }
}
