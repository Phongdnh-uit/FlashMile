package com.uit.se356.core.domain.vo.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.UserErrorCode;
import java.util.regex.Pattern;

public record Email(String value) {
  private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,}$";
  private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

  public Email {
    if (value == null || value.isBlank() || !PATTERN.matcher(value).matches()) {
      throw new AppException(UserErrorCode.INVALID_EMAIL_FORMAT);
    }
  }
}
