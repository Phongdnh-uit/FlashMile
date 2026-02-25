package com.uit.se356.core.application.user.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.user.result.UserProfileResult;

import java.util.HashMap;
import java.util.Map;

public record UpdateUserProfileCommand(
        String userId,
        String fullName,
        String email
) implements Command<UserProfileResult> {
    public UpdateUserProfileCommand {
        Map<String, Object> errors = new HashMap<>();

        // BR: Check Mandatory Fields
        if (userId == null || userId.isBlank()) {
            errors.put("userId", "User ID is required");
        }
        if (fullName == null || fullName.isBlank()) {
            errors.put("fullName", "Full name is required");
        }
        if (email == null || email.isBlank()) {
            errors.put("email", "Email is required");
        }

        if (!errors.isEmpty()) {
            throw new AppException(CommonErrorCode.VALIDATION_ERROR, errors);
        }
    }
}
