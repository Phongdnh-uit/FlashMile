package com.uit.se356.core.application.user.result;

import com.uit.se356.core.domain.entities.authentication.User;

public record UserProfileResult(
    String id,
    String fullName,
    String email,
    String phoneNumber,
    String status,
    boolean phoneVerified,
    boolean emailVerified) {
  // Method để chuyển đổi từ User entity sang UserProfileResult
  public static UserProfileResult fromUser(User user) {
    return new UserProfileResult(
        user.getId().value(),
        user.getFullName(),
        user.getEmail().value(),
        user.getPhoneNumber().value(),
        user.getStatus().name(),
        user.isPhoneVerified(),
        user.isEmailVerified());
  }
}
