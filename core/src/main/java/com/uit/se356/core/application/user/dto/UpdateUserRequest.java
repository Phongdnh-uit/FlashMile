package com.uit.se356.core.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank (message = "Full name is required")
        String fullName,
        @Email (message = "Invalid email format")
        String email
) {
}
