package com.uit.se356.core.application.user.service;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.user.dto.UpdateUserRequest;
import com.uit.se356.core.application.user.dto.UserResponse;
import com.uit.se356.core.application.user.mapper.UserMapper;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.vo.authentication.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse getMyProfile(String userId) {
        User user = userRepository.findById(new UserId(userId))
                .orElseThrow(() -> new AppException(CommonErrorCode.RESOURCE_NOT_FOUND, "User not found"));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(String userId, UpdateUserRequest request) {
        User user = userRepository.findById(new UserId(userId))
                .orElseThrow(() -> new AppException(CommonErrorCode.RESOURCE_NOT_FOUND, "User not found"));;
        user.updateProfile(request.fullName(), request.email());
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }
}