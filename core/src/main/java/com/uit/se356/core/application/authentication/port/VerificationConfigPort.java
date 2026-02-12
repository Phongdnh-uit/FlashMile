package com.uit.se356.core.application.authentication.port;

public interface VerificationConfigPort {
  long getEmailLinkExpiration();

  long getSmsOtpExpiration();

  long getPhoneVerifiedTokenExpiration();
}
