package com.uit.se356.core.application.internal.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.internal.result.DebugOtpResult;
import java.util.HashMap;
import java.util.Map;

public record DebugOtpQuery(String phoneNumber) implements Query<DebugOtpResult> {
  public DebugOtpQuery {
    Map<String, Object> errors = new HashMap<>();
    if (phoneNumber == null || phoneNumber.isBlank()) {
      errors.put("phoneNumber", "Phone number is required");
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
