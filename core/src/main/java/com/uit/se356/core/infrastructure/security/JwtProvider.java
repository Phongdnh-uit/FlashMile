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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtProvider implements TokenProvider {
  private static final int SECURE_TOKEN_BYTE_SIZE = 32; // 256 bits

  private final SecureRandom secureRandom = new SecureRandom();

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

  @Override
  public String generateSecureToken() {
    byte[] randomBytes = new byte[SECURE_TOKEN_BYTE_SIZE];
    secureRandom.nextBytes(randomBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
  }

  /** Hash một token sử dụng thuật toán SHA-256. Đây là hoạt động một chiều, không thể đảo ngược. */
  @Override
  public String hashToken(String token) {
    try {
      // Lấy một instance của MessageDigest cho SHA-256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      // Hash token (chuyển về dạng byte sử dụng UTF-8)
      byte[] hashedBytes = digest.digest(token.getBytes(StandardCharsets.UTF_8));

      // Chuyển mảng byte đã hash sang định dạng hex string
      return bytesToHex(hashedBytes);
    } catch (NoSuchAlgorithmException e) {
      // SHA-256 là thuật toán chuẩn và luôn có sẵn trong mọi JRE.
      throw new RuntimeException("Không thể tìm thấy thuật toán SHA-256", e);
    }
  }

  /** Hàm tiện ích để chuyển một mảng byte thành chuỗi Hex. */
  private String bytesToHex(byte[] bytes) {
    StringBuilder hexString = new StringBuilder(2 * bytes.length);
    for (byte b : bytes) {
      String hex = Integer.toHexString(0xff & b);
      if (hex.length() == 1) {
        hexString.append('0');
      }
      hexString.append(hex);
    }
    return hexString.toString();
  }
}
