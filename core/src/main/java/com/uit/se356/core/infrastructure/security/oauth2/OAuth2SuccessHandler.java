package com.uit.se356.core.infrastructure.security.oauth2;

import com.uit.se356.core.application.authentication.command.IssueTokenCommand;
import com.uit.se356.core.application.authentication.port.in.IssueTokenService;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.application.authentication.result.TokenPairResult;
import com.uit.se356.core.domain.vo.authentication.RoleId;
import com.uit.se356.core.infrastructure.config.AppProperties;
import com.uit.se356.core.infrastructure.security.CustomUserPrincipal;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
  private final AppProperties appProperties;
  private final IssueTokenService issueTokenHander;
  private final ObjectMapper mapper;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    CustomUserPrincipal principal = (CustomUserPrincipal) authentication.getPrincipal();
    // ============================ OLD CODE ============================
    // // Cần lấy được roleId từ role của UserPrincipal để issue token, bước chuyển đổi OAuth2 cần
    // map
    // // được role, role trong UserPrincipal là roleId
    // IssueTokenCommand command =
    //     new IssueTokenCommand(principal.getId(), new RoleId(principal.getRole()));
    // TokenPairResult tokenPair = issueTokenHander.issueToken(command);
    //
    // LoginResult loginResult =
    //     new LoginResult(
    //         tokenPair.accessToken(),
    //         tokenPair.refreshToken(),
    //         tokenPair.expiresIn(),
    //         tokenPair.tokenType());
    //
    // ApiResponse<LoginResult> apiResponse = ApiResponse.ok(loginResult, "OAuth2 login
    // successful");
    //
    // // Trả về SPA một trang HTML chứa JavaScript để gửi dữ liệu về cửa sổ cha và đóng cửa sổ hiện
    // // tại
    // String html =
    //     "<!DOCTYPE html>"
    //         + "<html>"
    //         + "<body>"
    //         + "<script>"
    //         + "const response = "
    //         + mapper.writeValueAsString(apiResponse)
    //         + ";"
    //         + "window.opener.postMessage(response, '"
    //         + appProperties.getFrontend().getBaseUrl()
    //         + "');"
    //         + "window.close();"
    //         + "</script>"
    //         + "</body>"
    //         + "</html>";
    //
    // response.setContentType("text/html");
    // response.setCharacterEncoding("UTF-8");
    // response.getWriter().write(html);
    // response.getWriter().flush();
    // ============================ REFACTOR ============================
    // Trả về exchange cho SPA, sau đó SPA sẽ gọi API để lấy access token và refresh token
    // String exchangeToken = UUID.randomUUID().toString();
    // String cacheKey = String.format(CacheKey.AUTH_EXCHANGE_TOKEN, exchangeToken);
    // authCacheRepository.set(
    //     cacheKey,
    //     principal.getId().value(),
    //     Duration.ofMinutes(
    //         5)); // Hard-code 5' do thường gọi endpoint này xong là sẽ lấy token ngay, nếu không
    // lấy
    // thì token sẽ tự hết hạn sau 5'

    // Trả về callback cho SPA
    /**
     * Trick (not recommend): Thay vì trả về exchange (phải code thêm endpoint), ta trả về
     * refreshToken dưới dạng cookie để tự gọi lại refresh lấy access token
     */
    IssueTokenCommand command =
        new IssueTokenCommand(principal.getId(), new RoleId(principal.getRole()));
    TokenPairResult tokenPair = issueTokenHander.issueToken(command);

    LoginResult loginResult =
        new LoginResult(
            tokenPair.accessToken(),
            tokenPair.refreshToken(),
            tokenPair.expiresIn(),
            tokenPair.tokenType());

    if (loginResult.refreshToken() != null && !loginResult.refreshToken().isEmpty()) {
      // Redirect với token trong cookie
      ResponseCookie cookie =
          ResponseCookie.from("refreshToken", loginResult.refreshToken())
              .httpOnly(true)
              .secure(true)
              .sameSite("None")
              .path("/")
              .maxAge(
                  Duration.ofMillis(appProperties.getSecurity().getJwt().getRefreshExpiration()))
              .build();
      String targetUrl =
          UriComponentsBuilder.fromUriString(appProperties.getFrontend().getBaseUrl())
              .path(appProperties.getFrontend().getOauth2CallbackPath())
              .queryParam("success", "true")
              .build()
              .toUriString();
      response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
      response.sendRedirect(targetUrl);
    } else {
      // Trường hợp còn lại sẽ không chứa access và refresh token trong body nên có thể trả về trên
      // params
      String targetUrl =
          UriComponentsBuilder.fromUriString(appProperties.getFrontend().getBaseUrl())
              .path(appProperties.getFrontend().getOauth2CallbackPath())
              .queryParam("success", "true")
              .queryParam("data", mapper.writeValueAsString(loginResult))
              .build()
              .toUriString();
      response.sendRedirect(targetUrl);
    }
  }
}
