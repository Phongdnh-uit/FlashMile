package com.uit.se356.core.application.user.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.user.command.UpdateUserProfileCommand;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.application.user.result.UserProfileResult;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.exception.UserErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserProfileHandler implements CommandHandler<UpdateUserProfileCommand, UserProfileResult> {

    private final UserRepository userRepository;

    public UpdateUserProfileHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileResult handle(UpdateUserProfileCommand command) {
        // Fetch User Data
        User user = userRepository.findById(new UserId(command.userId()))
                .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

        // BR: Check for Changes (MSG5)
        boolean isNameUnchanged = command.fullName().equals(user.getFullName());
        boolean isEmailUnchanged = command.email().equals(user.getEmail().value());

        if (isNameUnchanged && isEmailUnchanged) {
            throw new AppException(UserErrorCode.NO_CHANGE_DETECTED);
        }

        user.updateProfile(command.fullName(), command.email());
        User updatedUser = userRepository.update(user);
        return UserProfileResult.fromUser(updatedUser);
    }

}
