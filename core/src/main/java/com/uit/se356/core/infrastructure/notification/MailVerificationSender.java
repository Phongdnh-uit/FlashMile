package com.uit.se356.core.infrastructure.notification;

import com.uit.se356.common.services.MailService;
import com.uit.se356.core.application.authentication.port.VerificationSender;
import com.uit.se356.core.domain.vo.authentication.CodePurpose;
import com.uit.se356.core.domain.vo.authentication.VerificationChannel;
import com.uit.se356.core.infrastructure.config.AppProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class MailVerificationSender implements VerificationSender {
  private final MailService mailService;
  private final AppProperties appProperties;

  @Override
  public boolean support(VerificationChannel channel) {
    return VerificationChannel.EMAIL.equals(channel);
  }

  @Override
  public void send(String recipient, String code, CodePurpose purpose) {
    Map<String, Object> model = new HashMap<>();
    model.put("logoUrl", appProperties.getFrontend().getLogoUrl());
    switch (purpose) {
      case EMAIL_VERIFICATION:
        log.info("Email Verification to [{}]", recipient);
        String verifyUrl =
            appProperties.getFrontend().getBaseUrl()
                + appProperties.getFrontend().getVerifyEmailPath()
                + "?code="
                + code;
        model.put("brandName", "FlashMile");
        model.put("name", recipient);
        model.put("verifyLink", verifyUrl);

        mailService.sendEmailFromTemplate(
            recipient, "Xác thực tài khoản", "auth/verify-email", model);
        break;

      case FORGOT_PASSWORD:
        String resetPasswordUrl =
            appProperties.getFrontend().getBaseUrl()
                + appProperties.getFrontend().getResetPasswordPath()
                + "?code="
                + code;
        log.info("Sending OTP to [{}]", recipient);
        model.put("brandName", "FlashMile");
        model.put("name", recipient);
        model.put("resetLink", resetPasswordUrl);

        mailService.sendEmailFromTemplate(
            recipient, "Mã OTP quên mật khẩu", "auth/reset-password", model);
        break;
      default:
        log.warn("Unknown purpose SMS to [{}]: {}", recipient, code);
    }
  }
}
