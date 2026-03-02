package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.domain.entities.authentication.User;
import java.util.ArrayList;
import java.util.List;

public record OAuth2LoginCommand(
    String provider, String providerUserId, String email, String fullName, String verifiedPhone)
    implements Command<User> {
  public OAuth2LoginCommand {
    List<FieldError> errors = new ArrayList<>();
    if (provider == null || provider.isBlank()) {
      errors.add(
          new FieldError("provider", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"provider"}));
    }
    if (providerUserId == null || providerUserId.isBlank()) {
      errors.add(
          new FieldError(
              "providerUserId", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"provider user ID"}));
    }
    if (email == null || email.isBlank()) {
      errors.add(new FieldError("email", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"email"}));
    }
    if (fullName == null || fullName.isBlank()) {
      errors.add(
          new FieldError("fullName", CommonErrorCode.FIELD_REQUIRED.getMessageKey(), new Object[] {"full name"}));
    }
    // Bỏ qua kiểm tra verifiedPhone vì nó có thể là null hoặc blank nếu không được cung cấp, nó chỉ
    // cần khi lần đầu đăng nhập bằng OAuth2 và không có số điện thoại nào được liên kết với tài
    // khoản đó.
    if (!errors.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
    }
  }
}
