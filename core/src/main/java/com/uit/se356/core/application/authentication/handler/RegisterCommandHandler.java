package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.command.RegisterCommand;
import com.uit.se356.core.application.authentication.port.CacheRepository;
import com.uit.se356.core.application.authentication.port.PasswordEncoder;
import com.uit.se356.core.application.authentication.query.SendVerificationCodeQuery;
import com.uit.se356.core.application.authentication.result.RegisterResult;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.constants.CacheKey;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegisterCommandHandler implements CommandHandler<RegisterCommand, RegisterResult> {
  private final CacheRepository cacheRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final IdGenerator idGenerator;
  private final SendVerificationCodeHandler sendVerificationCodeHandler;

  @Override
  public RegisterResult handle(RegisterCommand command) {
    // Xác thực verificationToken từ cache để lấy số điện thoại đăng ký
    StringBuilder cacheKey =
        new StringBuilder(CacheKey.PHONE_VERIFIED_PREFIX)
            .append(":")
            .append(command.verificationToken());
    Optional<String> phoneNumberOpt = cacheRepository.get(cacheKey.toString());
    if (phoneNumberOpt.isEmpty()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE);
    }
    PhoneNumber phoneNumber = new PhoneNumber(phoneNumberOpt.get());
    Email email = new Email(command.email());
    UserId userId = new UserId(idGenerator.generate().toString());
    User user =
        User.create(
            userId,
            command.fullName(),
            email,
            passwordEncoder.encode(command.password()),
            phoneNumber,
            Instant.now(),
            userId);
    user.verifyPhone(userId);
    user = userRepository.save(user);
    // Xóa verificationToken khỏi cache sau khi đăng ký thành công
    cacheRepository.delete(cacheKey.toString());
    // Gửi email xác nhận
    SendVerificationCodeQuery sendEmailVerificationCodeQuery =
        new SendVerificationCodeQuery(
            CodePurpose.EMAIL_VERIFICATION, VerificationChannel.EMAIL, email.value());
    sendVerificationCodeHandler.handle(sendEmailVerificationCodeQuery);

    // Trả về kết quả đăng ký
    return new RegisterResult(
        user.getUserId().value(),
        user.getFullName(),
        user.getEmail().value(),
        user.getPhoneNumber().value(),
        user.isEmailVerified(),
        user.isPhoneVerified(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getCreatedBy().value(),
        user.getUpdatedBy().value());
  }
}
