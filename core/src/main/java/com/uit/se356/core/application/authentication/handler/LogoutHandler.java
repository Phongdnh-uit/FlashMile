package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.authentication.command.LogoutCommand;
import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.port.TokenProvider;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import java.util.Optional;

public class LogoutHandler implements CommandHandler<LogoutCommand, Void> {

  private final RefreshTokenRepository refreshTokenRepository;
  private final TokenProvider tokenProvider;

  public LogoutHandler(RefreshTokenRepository refreshTokenRepository, TokenProvider tokenProvider) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public Void handle(LogoutCommand command) {
    // Kiểm tra xem refresh token có hợp lê không
    String tokenHash = tokenProvider.hashToken(command.refreshToken());
    Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(tokenHash);
    if (!refreshTokenOpt.isPresent() || !refreshTokenOpt.get().isValid()) {
      throw new AppException(AuthErrorCode.INVALID_TOKEN);
    }
    // Revoked refresh token
    RefreshToken refreshToken = refreshTokenOpt.get();
    refreshToken.revoke();
    refreshTokenRepository.update(refreshToken);

    // Xóa cookie refresh token trên client, tầng presentation tự xử lý

    // TODO: cơ chế blacklist access token

    return null;
  }
}
