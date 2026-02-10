package com.uit.se356.core.application.user.dto;

public record UpdateUserRequest(
        String fullName,
        String email
) {
}
