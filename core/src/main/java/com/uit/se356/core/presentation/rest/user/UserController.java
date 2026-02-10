package com.uit.se356.core.presentation.rest.user;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.core.application.user.dto.UpdateUserRequest;
import com.uit.se356.core.application.user.dto.UserResponse;
import com.uit.se356.core.application.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyProfile() {
        // TODO: Lấy userId từ Security Context (JWT)
        String currentUserId = "user-id-from-token";

        return ApiResponse.ok(userService.getMyProfile(currentUserId), "Get profile success");
    }

    @PutMapping("/me")
    public ApiResponse<UserResponse> updateProfile(@RequestBody UpdateUserRequest request) {
        String currentUserId = "user-id-from-token";
        return ApiResponse.ok(userService.updateProfile(currentUserId, request), "Update profile success");
    }

    // API cho Admin
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.ok(userService.getAllUsers(), "Get all users success");
    }
}