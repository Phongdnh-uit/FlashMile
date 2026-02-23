package com.uit.se356.core.application.authentication.port.out;

public interface VerificationConfigPort {
  long getEmailLinkExpiration();

  long getSmsOtpExpiration();

  long getPhoneVerifiedTokenExpiration();

  long getForgotPasswordCodeExpiration();
}
