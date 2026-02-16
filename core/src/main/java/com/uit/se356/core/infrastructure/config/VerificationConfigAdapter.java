package com.uit.se356.core.infrastructure.config;

import com.uit.se356.core.application.authentication.port.VerificationConfigPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationConfigAdapter implements VerificationConfigPort {

  private final AppProperties appProperties;

  @Override
  public long getEmailLinkExpiration() {
    return appProperties.getVerification().getEmailLinkExpiration();
  }

  @Override
  public long getSmsOtpExpiration() {
    return appProperties.getVerification().getSmsOtpExpiration();
  }

  @Override
  public long getPhoneVerifiedTokenExpiration() {
    return appProperties.getVerification().getPhoneVerifiedTokenExpiration();
  }

  @Override
  public long getForgotPasswordCodeExpiration() {
    return appProperties.getVerification().getForgotPasswordCodeExpiration();
  }
}
