package com.uit.se356.core.infrastructure.notification;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.MailService;
import com.uit.se356.core.infrastructure.config.AppProperties;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailSender implements MailService {
  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  private final AppProperties appProperties;

  /**
   * @param to : recipient of the email
   * @param subject : subject of the email
   * @param content : content of the email, can be HTML or plain text
   * @param isMultipart : if the email contains attachments
   * @param isHtml : if the email content is HTML
   */
  @Async
  public void sendEmail(
      String to, String subject, String content, boolean isMultipart, boolean isHtml) {

    MimeMessage message = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, isMultipart, "UTF-8");
      helper.setTo(to);
      helper.setFrom(appProperties.getMail().getFrom());
      helper.setSubject(subject);
      helper.setText(content, isHtml);
      mailSender.send(message);
    } catch (Exception e) {
      log.error("Failed to send email to {} with subject {}: {}", to, subject, e.getMessage(), e);
      throw new AppException(CommonErrorCode.EMAIL_SENDING_FAILED);
    }
  }

  /**
   * @param to : recipient of the email
   * @param subject : subject of the email
   * @param templateName : name of the email template
   * @param model : model data to be used in the template
   */
  @Async
  public void sendEmailFromTemplate(
      String to, String subject, String templateName, Map<String, Object> model) {
    Context context = new Context();
    context.setVariables(model);
    String content = templateEngine.process(templateName, context);
    sendEmail(to, subject, content, false, true);
  }
}
