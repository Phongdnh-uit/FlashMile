package com.uit.se356.core.application.authentication.strategies.verification.send;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.port.VerificationConfigPort;
import com.uit.se356.core.application.authentication.port.VerificationRepository;
import com.uit.se356.core.application.authentication.port.VerificationSender;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.entities.authentication.Verification;
import com.uit.se356.core.domain.exception.UserErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;
import com.uit.se356.core.domain.vo.authentication.VerificationId;
import com.uit.se356.core.domain.vo.authentication.VerificationType;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ForgotPasswordSendingStrategy implements SendVerificationStrategy {
  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;
  private final VerificationConfigPort verificationConfigPort;
  private final IdGenerator idGenerator;

  public ForgotPasswordSendingStrategy(
      UserRepository userRepository,
      VerificationRepository verificationRepository,
      VerificationConfigPort verificationConfigPort,
      IdGenerator idGenerator) {
    this.userRepository = userRepository;
    this.verificationRepository = verificationRepository;
    this.verificationConfigPort = verificationConfigPort;
    this.idGenerator = idGenerator;
  }

  @Override
  public boolean support(CodePurpose purpose) {
    return CodePurpose.FORGOT_PASSWORD.equals(purpose);
  }

  @Override
  public void send(
      String recipient,
      CodePurpose purpose,
      VerificationChannel channel,
      List<VerificationSender> verificationSender) {
    // Để đơn giản, chỉ gửi OTP qua email
    Email email = new Email(recipient);
    // Kiểm tra tài khoản có tồn tại không
    Optional<User> userOpt = userRepository.findByEmail(email);
    if (userOpt.isEmpty()) {
      throw new AppException(UserErrorCode.USER_NOT_FOUND);
    }

    // Tạo mã và lưu
    long expirySeconds = verificationConfigPort.getForgotPasswordCodeExpiration();
    Verification verification =
        Verification.create(
            new VerificationId(idGenerator.generate().toString()),
            userOpt.get().getId(),
            VerificationType.RESET_PASSWORD,
            UUID.randomUUID().toString(),
            Instant.now().plusSeconds(expirySeconds));
    verificationRepository.save(verification);

    // Gửi mã OTP qua email
    verificationSender.stream()
        .filter(sender -> sender.support(VerificationChannel.EMAIL))
        .forEach(sender -> sender.send(recipient, verification.getCode(), purpose));
  }
}
