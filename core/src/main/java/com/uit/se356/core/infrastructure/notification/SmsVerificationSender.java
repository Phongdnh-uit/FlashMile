package com.uit.se356.core.infrastructure.notification;

import com.uit.se356.core.application.authentication.port.VerificationSender;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsVerificationSender implements VerificationSender {

  @Override
  public boolean support(VerificationChannel channel) {
    return VerificationChannel.PHONE.equals(channel);
  }

  @Override
  public void send(String recipient, String code, CodePurpose purpose) {
    // In a real application, integrate with SMS provider (Twilio, AWS SNS, etc.)
    switch (purpose) {
      case PHONE_VERIFICATION:
        log.info("Phone Verification SMS to [{}]: {}", recipient, code);
        System.out.println("=================================================");
        System.out.println("SMS to: " + recipient);
        System.out.println("Code: " + code);
        System.out.println("=================================================");
        break;
      default:
        log.warn("Unknown purpose SMS to [{}]: {}", recipient, code);
    }
  }
}
