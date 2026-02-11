package com.uit.se356.core.application.authentication.strategies;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.utils.OtpGenerator;
import com.uit.se356.core.application.authentication.port.CacheRepository;
import com.uit.se356.core.application.authentication.port.VerificationSender;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.constants.CacheKey;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneVerificationSendingStrategy implements SendVerificationStrategy {

  private final UserRepository userRepository;
  private final CacheRepository cacheRepository;

  @Override
  public boolean support(CodePurpose purpose) {
    return purpose == CodePurpose.PHONE_VERIFICATION;
  }

  @Override
  public void send(
      String recipient,
      CodePurpose purpose,
      VerificationChannel channel,
      List<VerificationSender> senders) {
    PhoneNumber phone = new PhoneNumber(recipient);
    // Kiểm tra xem số điện thoại đã được đăng ký chưa
    if (userRepository.existsByPhoneNumber(phone)) {
      throw new AppException(AuthErrorCode.PHONE_ALREADY_REGISTERED);
    }
    // tạo mã xác minh và lưu tạm cache 5 phút
    String otpCode = OtpGenerator.generateOtp(6);

    StringBuilder cacheKey =
        new StringBuilder(CacheKey.PHONE_VERIFICATION_CODE_PREFIX)
            .append(":")
            .append(phone.value());

    cacheRepository.set(cacheKey.toString(), otpCode, Duration.ofMinutes(5));

    // Gửi mã qua kênh
    // Chỉ hỗ trợ gửi qua PHONE trong trường hợp này
    senders.stream()
        .filter(sender -> sender.support(VerificationChannel.PHONE))
        .forEach(
            sender -> {
              sender.send(recipient, otpCode);
            });
  }
}
