package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.port.PasswordEncoder;
import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.port.TokenProvider;
import com.uit.se356.core.application.authentication.query.LoginQuery;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.exception.UserErrorCode;
import com.uit.se356.core.domain.vo.authentication.Email;
import com.uit.se356.core.domain.vo.authentication.PhoneNumber;
import com.uit.se356.core.domain.vo.authentication.RefreshTokenId;
import com.uit.se356.core.domain.vo.authentication.UserStatus;
import java.time.Instant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LoginQueryHandler implements QueryHandler<LoginQuery, LoginResult> {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final IdGenerator idGenerator;

  @Override
  public LoginResult handle(LoginQuery query) {
    // Truy xuất người dùng bằng email hoặc số điện thoại
    User user;
    if (query.credentialId().contains("@")) {
      try {
        Email email = new Email(query.credentialId());
        user =
            userRepository
                .findByEmail(email)
                .orElseThrow(() -> new AppException(AuthErrorCode.INVALID_CREDENTIALS));
      } catch (AppException e) {
        if (e.getErrorCode() == UserErrorCode.INVALID_EMAIL_FORMAT) {
          throw new AppException(AuthErrorCode.CREDENTIAL_ID_INVALID);
        }
        throw e;
      }
    } else {
      try {
        PhoneNumber phoneNumber = new PhoneNumber(query.credentialId());
        user =
            userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(AuthErrorCode.INVALID_CREDENTIALS));
      } catch (AppException e) {
        if (e.getErrorCode() == UserErrorCode.INVALID_PHONE_FORMAT) {
          throw new AppException(AuthErrorCode.CREDENTIAL_ID_INVALID);
        }
        throw e;
      }
    }

    // Kiểm tra trạng thái người dùng
    if (user.getStatus() == UserStatus.UNVERIFIED) {
      throw new AppException(AuthErrorCode.USER_UNVERIFIED);
    }
    if (user.getStatus() == UserStatus.BLOCKED) {
      throw new AppException(AuthErrorCode.USER_BLOCKED);
    }

    // Xác thực mật khẩu
    if (!passwordEncoder.matches(query.password(), user.getPasswordHash())) {
      throw new AppException(AuthErrorCode.INVALID_CREDENTIALS);
    }

    // Tạo token và lưu phiên đăng nhập
    String refreshToken = tokenProvider.generateRefreshToken(user.getUserId().toString());
    String tokenHash = Base64.getEncoder().encodeToString(refreshToken.getBytes());
    RefreshToken rToken =
        RefreshToken.create(
            new RefreshTokenId(idGenerator.generate().toString()),
            user.getUserId(),
            tokenHash,
            Instant.now().plusMillis(tokenProvider.getRefreshTokenExpiryDuration()));
    rToken = refreshTokenRepository.save(rToken);

    String accessToken =
        tokenProvider.generateToken(
            user.getUserId().toString(),
            user.getEmail().value(),
            "USER"); // TODO: thêm bảng role và xử lý sau

    return new LoginResult(
        accessToken, refreshToken, tokenProvider.getTokenExpiryDuration(), "Bearer");
  }
}
