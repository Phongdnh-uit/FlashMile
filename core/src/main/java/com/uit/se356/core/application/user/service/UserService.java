package com.uit.se356.core.application.user.service;

import com.uit.se356.core.application.user.dto.UpdateUserRequest;
import com.uit.se356.core.application.user.dto.UserResponse;
import java.util.List;

public interface UserService {
    UserResponse getMyProfile(String userId);
    UserResponse updateProfile(String userId, UpdateUserRequest request);
    List<UserResponse> getAllUsers(); // Dành cho Admin
}