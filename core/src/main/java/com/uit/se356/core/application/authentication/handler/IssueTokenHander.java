package com.uit.se356.core.application.authentication.handler;

import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.authentication.command.IssueTokenCommand;
import com.uit.se356.core.application.authentication.port.RefreshTokenRepository;
import com.uit.se356.core.application.authentication.port.TokenProvider;
import com.uit.se356.core.application.authentication.result.TokenPairResult;
import com.uit.se356.core.domain.entities.authentication.RefreshToken;
import com.uit.se356.core.domain.vo.authentication.RefreshTokenId;
import java.time.Instant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IssueTokenHander implements CommandHandler<IssueTokenCommand, TokenPairResult> {

  private final TokenProvider tokenProvider;
  private final RefreshTokenRepository refreshTokenRepository;
  private final IdGenerator idGenerator;

  @Override
  public TokenPairResult handle(IssueTokenCommand command) {
    // Tạo token và lưu phiên đăng nhập
    String refreshToken = tokenProvider.generateToken(command.userId());
    String tokenHash = Base64.getEncoder().encodeToString(refreshToken.getBytes());
    RefreshToken rToken =
        RefreshToken.create(
            new RefreshTokenId(idGenerator.generate().toString()),
            command.userId(),
            tokenHash,
            Instant.now().plusMillis(tokenProvider.getRefreshTokenExpiryDuration()));
    rToken = refreshTokenRepository.save(rToken);

    String accessToken = tokenProvider.generateToken(command.userId());

    return new TokenPairResult(
        accessToken, refreshToken, tokenProvider.getTokenExpiryDuration(), "Bearer");
  }
}
