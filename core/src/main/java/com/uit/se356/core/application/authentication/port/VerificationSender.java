package com.uit.se356.core.application.authentication.port;

import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;

public interface VerificationSender {
  boolean support(VerificationChannel channel);

  void send(String recipient, String code, CodePurpose purpose);
}
