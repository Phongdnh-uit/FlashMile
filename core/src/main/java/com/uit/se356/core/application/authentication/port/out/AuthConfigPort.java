package com.uit.se356.core.application.authentication.port.out;

public interface AuthConfigPort {
  long getEmailLinkExpiration();

  long getSmsOtpExpiration();

  long getPhoneVerifiedTokenExpiration();

  long getForgotPasswordCodeExpiration();

  long getPrechallengeTokenExpiration();

  long getMfaChallengeExpiration();
}
