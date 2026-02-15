package com.uit.se356.core.infrastructure.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.application.authentication.port.TokenProvider;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.config.AppProperties;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider implements TokenProvider {

  private final AppProperties appProperties;

  public String generateToken(UserId userId) {
    try {
      JWSSigner signer = new MACSigner(appProperties.getSecurity().getJwt().getSecretKey());

      JWTClaimsSet claimsSet =
          new JWTClaimsSet.Builder()
              .subject(userId.value())
              .issuer("flashmile")
              .issueTime(new Date())
              .expirationTime(
                  new Date(
                      System.currentTimeMillis()
                          + appProperties.getSecurity().getJwt().getExpiration()))
              .jwtID(UUID.randomUUID().toString())
              .build();

      SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

      signedJWT.sign(signer);

      return signedJWT.serialize();
    } catch (JOSEException e) {
      log.error("Error generating token", e);
      throw new AppException(AuthErrorCode.UNCATEGORIZED_EXCEPTION);
    }
  }

  @Override
  public Long getTokenExpiryDuration() {
    return appProperties.getSecurity().getJwt().getExpiration();
  }

  @Override
  public Long getRefreshTokenExpiryDuration() {
    return appProperties.getSecurity().getJwt().getRefreshExpiration();
  }
}
