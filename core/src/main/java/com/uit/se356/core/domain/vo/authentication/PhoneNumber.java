package com.uit.se356.core.domain.vo.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.UserErrorCode;
import java.util.regex.Pattern;

public record PhoneNumber(String value) {
  private static final String PHONE_PATTERN = "^\\d{10,11}$";
  private static final Pattern PATTERN = Pattern.compile(PHONE_PATTERN);

  public PhoneNumber {
    if (value == null || value.isBlank() || !PATTERN.matcher(value).matches()) {
      throw new AppException(UserErrorCode.INVALID_PHONE_FORMAT);
    }
  }
}
