package com.uit.se356.core.infrastructure.security;

import com.uit.se356.common.utils.EncryptUtil;
import com.uit.se356.core.infrastructure.config.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EncryptService
    implements com.uit.se356.core.application.authentication.port.out.EncryptService {

  private final AppProperties appProperties;

  @Override
  public String encrypt(String plainText) {
    String secretKey = appProperties.getSecurity().getEncryptionSecretKey();
    String salt = appProperties.getSecurity().getEncryptionSalt();
    return EncryptUtil.encrypt(plainText, secretKey, salt);
  }

  @Override
  public String decrypt(String cipherText) {
    String secretKey = appProperties.getSecurity().getEncryptionSecretKey();
    String salt = appProperties.getSecurity().getEncryptionSalt();
    return EncryptUtil.decrypt(cipherText, secretKey, salt);
  }
}
