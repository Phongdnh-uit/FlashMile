package com.uit.se356.core.infrastructure.security.oauth2;

import com.uit.se356.common.dto.ErrorResponse;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import com.uit.se356.core.infrastructure.config.AppProperties;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

  private final ObjectMapper mapper;
  private final AppProperties appProperties;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {
    ErrorResponse errorResponse =
        new ErrorResponse(
            request.getRequestURI(),
            401,
            exception.getMessage(),
            null,
            AuthErrorCode.OAUTH2_AUTHORIZATION_REQUEST_FAILED.getCode());

    String html =
        "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "const response = "
            + mapper.writeValueAsString(errorResponse)
            + ";\n"
            + "window.opener.postMessage(response, '"
            + appProperties.getFrontend().getBaseUrl()
            + "');\n"
            + "window.close();\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

    response.setContentType("text/html");
    response.getWriter().write(html);
  }
}
