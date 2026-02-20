package com.uit.se356.core.domain.vo.authentication;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.utils.ValidationUtil;
import com.uit.se356.core.domain.exception.UserErrorCode;
import java.util.regex.Pattern;

public record PhoneNumber(String value) {
  private static final Pattern E164_PATTERN = Pattern.compile("^\\+[1-9]\\d{1,14}$");

  public PhoneNumber {
    // Ép buộc phải truyền số điện thoại theo dạng E.164
    // Tạm chấp nhận đánh đổi clean code để đảm bảo tính đúng đắn của dữ liệu ngay từ đầu
    if (value == null
        || value.isBlank()
        || !E164_PATTERN.matcher(value).matches()
        || !ValidationUtil.isValidPhoneNumber(value, null)) {
      throw new AppException(UserErrorCode.INVALID_PHONE_FORMAT);
    }
  }
}
