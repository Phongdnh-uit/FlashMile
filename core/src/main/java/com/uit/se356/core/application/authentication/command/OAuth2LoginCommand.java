package com.uit.se356.core.application.authentication.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import java.util.HashMap;
import java.util.Map;

public record OAuth2LoginCommand(
    String provider, String providerUserId, String email, String fullName, String verifiedPhone)
    implements Command<User> {
  public OAuth2LoginCommand {
    Map<String, Object> errors = new HashMap<>();
    if (provider == null || provider.isBlank()) {
      errors.put("provider", "Provider is required");
    }
    if (providerUserId == null || providerUserId.isBlank()) {
      errors.put("providerUserId", "Provider user ID is required");
    }
    if (email == null || email.isBlank()) {
      errors.put("email", "Email is required");
    }
    if (fullName == null || fullName.isBlank()) {
      errors.put("fullName", "Full name is required");
    }
    // Bỏ qua kiểm tra verifiedPhone vì nó có thể là null hoặc blank nếu không được cung cấp, nó chỉ
    // cần khi lần đầu đăng nhập bằng OAuth2 và không có số điện thoại nào được liên kết với tài
    // khoản đó.
    if (!errors.isEmpty()) {
      throw new AppException(AuthErrorCode.INVALID_CREDENTIALS, errors);
    }
  }
}
