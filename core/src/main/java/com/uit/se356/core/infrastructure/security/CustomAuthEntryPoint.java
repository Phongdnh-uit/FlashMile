package com.uit.se356.core.infrastructure.security;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class CustomAuthEntryPoint implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper;
  private final MessageSource messageSource;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    Throwable cause = authException.getCause();
    ErrorResponse errorResponse = null;
    if (cause != null && cause instanceof BadJwtException jwtValidationException) {
      boolean isExpired = jwtValidationException.getMessage().contains("expired");
      if (isExpired) {
        errorResponse =
            new ErrorResponse(
                request.getRequestURI(),
                AuthErrorCode.TOKEN_EXPIRED.getHttpStatus(),
                messageSource.getMessage(
                    AuthErrorCode.TOKEN_EXPIRED.getMessageKey(),
                    null,
                    LocaleContextHolder.getLocale()),
                null,
                AuthErrorCode.TOKEN_EXPIRED.getCode());
      } else {
        errorResponse =
            new ErrorResponse(
                request.getRequestURI(),
                AuthErrorCode.INVALID_TOKEN.getHttpStatus(),
                messageSource.getMessage(
                    AuthErrorCode.INVALID_TOKEN.getMessageKey(),
                    null,
                    LocaleContextHolder.getLocale()),
                null,
                AuthErrorCode.INVALID_TOKEN.getCode());
      }
    } else {
      while (cause.getCause() != null) {
        cause = cause.getCause();
      }
      errorResponse =
          new ErrorResponse(
              request.getRequestURI(),
              AuthErrorCode.AUTHENTICATION_REQUIRED.getHttpStatus(),
              messageSource.getMessage(
                  AuthErrorCode.AUTHENTICATION_REQUIRED.getMessageKey(),
                  null,
                  LocaleContextHolder.getLocale()),
              null,
              AuthErrorCode.AUTHENTICATION_REQUIRED.getCode());
    }
    // TODO: check các custom exception

    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    objectMapper.writeValue(response.getOutputStream(), errorResponse);
  }
}
