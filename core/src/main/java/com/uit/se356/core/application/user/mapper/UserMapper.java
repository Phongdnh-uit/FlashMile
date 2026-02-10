package com.uit.se356.core.application.user.mapper;

import com.uit.se356.core.application.user.dto.UserResponse;
import com.uit.se356.core.domain.entities.authentication.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User domain) {
        if (domain == null) return null;
        return new UserResponse(
                domain.getUserId().value(),
                domain.getFullName(),
                domain.getEmail().value(),
                domain.getPhoneNumber().value(),
                domain.getStatus(),
                domain.isPhoneVerified(),
                domain.isEmailVerified(),
                domain.getCreatedAt()
        );
    }
}
