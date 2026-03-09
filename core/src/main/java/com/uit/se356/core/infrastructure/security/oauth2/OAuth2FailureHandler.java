package com.uit.se356.core.infrastructure.security.oauth2;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.common.dto.FieldError;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.infrastructure.config.AppProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper mapper;
  private final AppProperties appProperties;
  private final MessageSource messageSource;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {
    AppException ex = findAppException(exception);

    ErrorResponse errorResponse = null;

    if (ex != null) {

      String message =
          messageSource.getMessage(
              ex.getErrorCode().getMessageKey(), null, LocaleContextHolder.getLocale());
      // Chuyển detail trong exception nếu là dạng map
      Map<String, String> errors = new HashMap<>();
      // Chuyển detail nếu là list field error
      if (ex.getDetails() instanceof List list
          && !list.isEmpty()
          && list.get(0) instanceof FieldError) {
        @SuppressWarnings("unchecked")
        List<FieldError> fieldErrors = (List<FieldError>) ex.getDetails();
        fieldErrors.forEach(
            fieldError -> {
              String localizedMessage =
                  messageSource.getMessage(
                      fieldError.messageKey(), fieldError.args(), LocaleContextHolder.getLocale());
              errors.put(fieldError.field(), localizedMessage);
            });
      }

      errorResponse =
          new ErrorResponse(
              request.getRequestURI(),
              ex.getErrorCode().getHttpStatus(),
              message.isBlank() ? ex.getMessage() : message,
              errors,
              ex.getErrorCode().getCode());
    } else {
      errorResponse =
          new ErrorResponse(
              request.getRequestURI(),
              401,
              exception.getMessage(),
              null,
              AuthErrorCode.OAUTH2_AUTHORIZATION_REQUEST_FAILED.getCode());
    }

    log.error(
        "OAuth2 authentication failure: {}, errorResponse: {}",
        exception.getMessage(),
        mapper.writeValueAsString(errorResponse));

    String html =
        "<!DOCTYPE html>"
            + "<html>"
            + "<body>"
            + "<script>"
            + "const response = "
            + mapper.writeValueAsString(errorResponse)
            + ";"
            + "window.opener.postMessage(response, '"
            + appProperties.getFrontend().getBaseUrl()
            + "');"
            + "window.close();"
            + "</script>"
            + "</body>"
            + "</html>";

    response.setContentType("text/html");
    response.setCharacterEncoding("UTF-8");
    mapper.writeValue(response.getWriter(), html);
  }

  private AppException findAppException(Throwable throwable) {
    if (throwable instanceof AppException appEx) return appEx;
    if (throwable.getCause() != null) return findAppException(throwable.getCause());
    return null;
  }
}
