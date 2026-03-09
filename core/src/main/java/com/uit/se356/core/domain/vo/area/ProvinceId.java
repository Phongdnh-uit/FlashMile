package com.uit.se356.core.domain.vo.area;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AreaErrorCode;

public record ProvinceId(String value) {
  public ProvinceId {
    if (value == null || value.isBlank()) {
      throw new AppException(AreaErrorCode.INVALID_PROVINCE_ID);
    }
  }
}
