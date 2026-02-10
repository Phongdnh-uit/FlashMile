package com.uit.se356.core.application.user.dto;

import com.uit.se356.core.domain.vo.authentication.UserStatus;

import java.time.Instant;

public record UserResponse(
        String id,
        String fullName,
        String email,
        String phoneNumber,
        UserStatus status,
        boolean phoneVerified,
        boolean emailVerified,
        Instant createdAt
) {
}
