package com.uit.se356.core.application.authentication.strategies.verification.process;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.authentication.port.CacheRepository;
import com.uit.se356.core.application.authentication.port.VerificationConfigPort;
import com.uit.se356.core.application.authentication.result.VerificationResult;
import com.uit.se356.core.domain.constants.CacheKey;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

public class PhoneVerificationProcessingStrategy implements ProcessVerificationStrategy {
  private final CacheRepository cacheRepository;
  private final VerificationConfigPort verificationConfigPort;

  public PhoneVerificationProcessingStrategy(
      CacheRepository cacheRepository, VerificationConfigPort verificationConfigPort) {
    this.cacheRepository = cacheRepository;
    this.verificationConfigPort = verificationConfigPort;
  }

  @Override
  public boolean support(CodePurpose purpose) {
    return purpose == CodePurpose.PHONE_VERIFICATION;
  }

  @Override
  public VerificationResult process(CodePurpose purpose, String recipient, String code) {
    // Cần check recipient ở đây vì query có thể không hợp lệ
    if (recipient == null || recipient.isBlank()) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE_REQUEST);
    }
    PhoneNumber phoneNumber = new PhoneNumber(recipient);
    //  Kiểm tra mã xác minh từ cache
    StringBuilder cacheKey =
        new StringBuilder(CacheKey.PHONE_VERIFICATION_CODE_PREFIX)
            .append(":")
            .append(phoneNumber.value());
    Optional<String> cachedCode = cacheRepository.get(cacheKey.toString());
    if (cachedCode.isEmpty() || !cachedCode.get().equals(code)) {
      throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE);
    }
    // Xoá mã xác minh khỏi cache sau khi xác minh thành công
    cacheRepository.delete(cacheKey.toString());
    // Tạo token chứng minh đã xác minh số điện thoại
    // Đưa số điện thoại vào cache với token
    String token = UUID.randomUUID().toString();
    StringBuilder verifiedCacheKey =
        new StringBuilder(CacheKey.PHONE_VERIFIED_PREFIX).append(":").append(token);

    long expirationSeconds = verificationConfigPort.getPhoneVerifiedTokenExpiration();
    cacheRepository.set(
        verifiedCacheKey.toString(), recipient, Duration.ofSeconds(expirationSeconds)); // 10 phút
    // Trả về token đã tạo
    return new VerificationResult(token, expirationSeconds);
  }
}
