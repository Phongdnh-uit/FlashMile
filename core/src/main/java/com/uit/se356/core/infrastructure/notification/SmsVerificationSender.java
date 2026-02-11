package com.uit.se356.core.infrastructure.notification;

import com.uit.se356.core.application.authentication.port.VerificationSender;
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
  public void send(String recipient, String message) {
    // In a real application, integrate with SMS provider (Twilio, AWS SNS, etc.)
    // For now, log to console/file
    log.info("Sending SMS to [{}]: {}", recipient, message);
    System.out.println("=================================================");
    System.out.println("SMS to: " + recipient);
    System.out.println("Message: " + message);
    System.out.println("=================================================");
  }
}
