package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.command.IssueTokenCommand;
import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.port.TokenProvider;
import com.uit.se356.core.application.authentication.result.TokenPairResult;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.domain.vo.authentication.RefreshTokenId;
import java.time.Instant;

public class IssueTokenService {

  private final TokenProvider tokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final IdGenerator idGenerator;

  public IssueTokenService(
      TokenProvider tokenProvider,
      RefreshTokenRepository refreshTokenRepository,
      IdGenerator idGenerator) {
    this.tokenProvider = tokenProvider;
    this.refreshTokenRepository = refreshTokenRepository;
    this.idGenerator = idGenerator;
  }

  public TokenPairResult handle(IssueTokenCommand command) {
    // Tạo token và lưu phiên đăng nhập
    String refreshToken = tokenProvider.generateSecureToken();
    String tokenHash = tokenProvider.hashToken(refreshToken);
    RefreshToken rToken =
        RefreshToken.create(
            new RefreshTokenId(idGenerator.generate().toString()),
            command.userId(),
            tokenHash,
            Instant.now().plusMillis(tokenProvider.getRefreshTokenExpiryDuration()));
    rToken = refreshTokenRepository.create(rToken);

    String accessToken = tokenProvider.generateToken(command.userId());

    return new TokenPairResult(
        accessToken, refreshToken, tokenProvider.getTokenExpiryDuration(), "Bearer");
  }

  public String hashToken(String token) {
    return tokenProvider.hashToken(token);
  }
}
