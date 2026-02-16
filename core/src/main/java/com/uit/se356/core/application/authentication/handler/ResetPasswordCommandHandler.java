package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.authentication.command.ResetPasswordCommand;
import com.uit.se356.core.application.authentication.port.PasswordEncoder;
import com.uit.se356.core.application.authentication.port.VerificationRepository;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.entities.authentication.Verification;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.exception.UserErrorCode;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ResetPasswordCommandHandler implements CommandHandler<ResetPasswordCommand, Void> {
  private final VerificationRepository verificationRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public Void handle(ResetPasswordCommand command) {
    // Kiểm tra mã xác thực có hợp lệ không
    Optional<Verification> verificationOpt =
        verificationRepository.findByTokenAndType(
            command.verificationToken(), VerificationType.RESET_PASSWORD);
    if (verificationOpt.isEmpty() || verificationOpt.get().isExpired()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE);
    }
    // Cập nhật mật khẩu mới cho người dùng
    Optional<User> userOpt = userRepository.findById(verificationOpt.get().getUserId());
    if (userOpt.isEmpty()) {
      throw new AppException(UserErrorCode.USER_NOT_FOUND);
    }
    User user = userOpt.get();
    user.changePassword(passwordEncoder.encode(command.newPassword()), user.getUserId());
    userRepository.save(user);
    // Xóa mã xác thực sau khi sử dụng
    verificationRepository.delete(verificationOpt.get());
    return null;
  }
}
