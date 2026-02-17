package com.uit.se356.core.infrastructure.security.oauth2;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.core.application.authentication.command.IssueTokenCommand;
import com.uit.se356.core.application.authentication.handler.IssueTokenService;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.application.authentication.result.TokenPairResult;
import com.uit.se356.core.infrastructure.config.AppProperties;
import com.uit.se356.core.infrastructure.security.CustomUserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
  private final ObjectMapper mapper;
  private final IssueTokenService issueTokenHander;
  private final AppProperties appProperties;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
    IssueTokenCommand command = new IssueTokenCommand(principal.getId());
    TokenPairResult tokenPair = issueTokenHander.handle(command);

    LoginResult loginResult =
        new LoginResult(
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            tokenPair.expiresIn(),
            tokenPair.tokenType());

    ApiResponse<LoginResult> apiResponse = ApiResponse.ok(loginResult, "OAuth2 login successful");

    // Trả về SPA một trang HTML chứa JavaScript để gửi dữ liệu về cửa sổ cha và đóng cửa sổ hiện
    // tại
    String html =
        "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "const response = "
            + mapper.writeValueAsString(apiResponse)
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
