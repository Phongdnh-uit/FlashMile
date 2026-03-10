package com.uit.se356.core.infrastructure.config;

import com.uit.se356.core.application.authentication.port.out.AuthConfigPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificationConfigAdapter implements AuthConfigPort {

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

  @Override
  public long getPrechallengeTokenExpiration() {
    return appProperties.getVerification().getPrechallengeTokenExpiration();
  }

  @Override
  public long getMfaChallengeExpiration() {
    return appProperties.getVerification().getMfaChallengeExpiration();
  }
}
