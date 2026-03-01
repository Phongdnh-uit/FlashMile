package com.uit.se356.core.application.internal.query;

import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.internal.result.DebugOtpResult;
import java.util.ArrayList;
import java.util.List;

public record DebugOtpQuery(String phoneNumber) implements Query<DebugOtpResult> {
  public DebugOtpQuery {
    List<FieldError> errors = new ArrayList<>();
    if (phoneNumber == null || phoneNumber.isBlank()) {
      errors.add(
          new FieldError(
              "phoneNumber", "validation.field.required", new Object[] {"phone number"}));
    }
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
