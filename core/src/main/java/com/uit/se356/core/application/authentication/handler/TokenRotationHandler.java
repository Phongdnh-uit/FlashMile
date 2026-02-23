package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.authentication.command.IssueTokenCommand;
import com.uit.se356.core.application.authentication.command.TokenRotationCommand;
import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.port.in.IssueTokenService;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.application.authentication.result.TokenPairResult;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import java.util.Optional;

public class TokenRotationHandler implements CommandHandler<TokenRotationCommand, LoginResult> {
  private final RefreshTokenRepository refreshTokenRepository;
  private final IssueTokenService issueTokenHander;
  private final UserRepository userRepository;

  public TokenRotationHandler(
      RefreshTokenRepository refreshTokenRepository,
      IssueTokenService issueTokenHander,
      UserRepository userRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.issueTokenHander = issueTokenHander;
    this.userRepository = userRepository;
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

    // Kiểm tra xem user có tồn tại không
    Optional<User> userOpt = userRepository.findById(refreshToken.getUserId());
    if (userOpt.isEmpty()) {
      throw new AppException(AuthErrorCode.INVALID_TOKEN);
    }

    // Không xóa refresh token mà revoke nó để đánh dấu là không còn hợp lệ, sau 1 tuần sẽ cronjob
    // xóa hoàn toàn
    refreshToken.revoke();
    refreshTokenRepository.update(refreshToken);

    // Issue token mới với thông tin người dùng mới (có thể role đã thay đổi roles, permissions,...)
    IssueTokenCommand issueTokenCommand =
        new IssueTokenCommand(userOpt.get().getId(), userOpt.get().getRoleId());
    TokenPairResult tokenPair = issueTokenHander.issueToken(issueTokenCommand);

    LoginResult loginResult =
        new LoginResult(
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            tokenPair.expiresIn(),
            tokenPair.tokenType());

    return loginResult;
  }
}
