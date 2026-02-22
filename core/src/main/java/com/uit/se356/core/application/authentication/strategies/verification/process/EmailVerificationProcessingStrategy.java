package com.uit.se356.core.application.authentication.strategies.verification.process;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.authentication.port.VerificationRepository;
import com.uit.se356.core.application.authentication.result.VerificationResult;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.entities.authentication.Verification;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import java.util.Optional;

public class EmailVerificationProcessingStrategy implements ProcessVerificationStrategy {
  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;

  public EmailVerificationProcessingStrategy(
      UserRepository userRepository, VerificationRepository verificationRepository) {
    this.userRepository = userRepository;
    this.verificationRepository = verificationRepository;
  }

  @Override
  public boolean support(CodePurpose purpose) {
    return purpose == CodePurpose.EMAIL_VERIFICATION;
  }

  @Override
  public VerificationResult process(CodePurpose purpose, String recipient, String code) {
    // Kiểm tra mã xác thực
    Optional<Verification> verificationOpt =
        verificationRepository.findByTokenAndType(code, VerificationType.EMAIL_VERIFICATION);
    if (verificationOpt.isEmpty() || verificationOpt.get().isExpired()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE);
    }
    Optional<User> userOpt = userRepository.findById(verificationOpt.get().getUserId());
    if (userOpt.isEmpty()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE);
    }
    User user = userOpt.get();
    // Cập nhật trạng thái xác thực email của người dùng
    user.verifyEmail();
    user.updateStatus(UserStatus.ACTIVE);
    userRepository.update(user);
    // Xoá mã xác thực sau khi sử dụng
    verificationRepository.delete(verificationOpt.get());

    return null;
  }
}
