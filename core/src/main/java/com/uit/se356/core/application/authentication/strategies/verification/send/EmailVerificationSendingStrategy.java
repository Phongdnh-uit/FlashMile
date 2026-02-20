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
import java.util.UUID;

public class EmailVerificationSendingStrategy implements SendVerificationStrategy {

  private final UserRepository userRepository;
  private final VerificationRepository verificationRepository;
  private final IdGenerator idGenerator;
  private final VerificationConfigPort verificationConfigPort;

  public EmailVerificationSendingStrategy(
      UserRepository userRepository,
      VerificationRepository verificationRepository,
      IdGenerator idGenerator,
      VerificationConfigPort verificationConfigPort) {
    this.userRepository = userRepository;
    this.verificationRepository = verificationRepository;
    this.idGenerator = idGenerator;
    this.verificationConfigPort = verificationConfigPort;
  }

  @Override
  public boolean support(CodePurpose purpose) {
    return purpose == CodePurpose.EMAIL_VERIFICATION;
  }

  @Override
  public void send(
      String recipient,
      CodePurpose purpose,
      VerificationChannel channel,
      List<VerificationSender> senders) {
    Email email = new Email(recipient);
    // Kiểm tra email đã tồn tại chưa
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new AppException(UserErrorCode.USER_NOT_FOUND));

    // Xóa mã cũ nếu có
    verificationRepository.deleteByUserIdAndType(user.getId(), VerificationType.EMAIL_VERIFICATION);

    // Tạo mã mới
    // Sử dụng cấu hình expire
    long expirationSeconds = verificationConfigPort.getEmailLinkExpiration();
    Instant expiresAt = Instant.now().plusSeconds(expirationSeconds);
    Verification verification =
        Verification.create(
            new VerificationId(idGenerator.generate().toString()),
            user.getId(),
            VerificationType.EMAIL_VERIFICATION,
            UUID.randomUUID().toString(),
            expiresAt);

    Verification savedVerification = verificationRepository.create(verification);

    // Gửi email
    senders.stream()
        .filter(sender -> sender.support(VerificationChannel.EMAIL))
        .forEach(
            sender -> {
              sender.send(recipient, savedVerification.getCode(), purpose);
            });
  }
}
