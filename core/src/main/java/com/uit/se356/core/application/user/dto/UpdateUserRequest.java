package com.uit.se356.core.application.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
        @NotBlank String fullName,
        @Email String email
) {
}
