package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.authentication.command.IssueTokenCommand;
import com.uit.se356.core.application.authentication.command.TokenRotationCommand;
import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.application.authentication.result.TokenPairResult;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.domain.exception.AuthErrorCode;

public class TokenRotationHandler implements CommandHandler<TokenRotationCommand, LoginResult> {
  private final RefreshTokenRepository refreshTokenRepository;
  private final IssueTokenService issueTokenHander;

  public TokenRotationHandler(
      RefreshTokenRepository refreshTokenRepository, IssueTokenService issueTokenHander) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.issueTokenHander = issueTokenHander;
  }

  @Override
  public LoginResult handle(TokenRotationCommand command) {
    // Kiểm tra xem refresh token có tồn tại và hợp lệ không
    String tokenHash = issueTokenHander.hashToken(command.refreshToken());
    RefreshToken refreshToken =
        refreshTokenRepository
            .findByToken(tokenHash)
            .orElseThrow(() -> new AppException(AuthErrorCode.INVALID_TOKEN));
    if (!refreshToken.isValid()) {
      throw new AppException(AuthErrorCode.INVALID_TOKEN);
    }

    // Không xóa refresh token mà revoke nó để đánh dấu là không còn hợp lệ, sau 1 tuần sẽ cronjob
    // xóa hoàn toàn
    refreshToken.revoke();
    refreshTokenRepository.update(refreshToken);

    // Issue token mới
    IssueTokenCommand issueTokenCommand = new IssueTokenCommand(refreshToken.getUserId());
    TokenPairResult tokenPair = issueTokenHander.handle(issueTokenCommand);

    LoginResult loginResult =
        new LoginResult(
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            tokenPair.expiresIn(),
            tokenPair.tokenType());

    return loginResult;
  }
}
