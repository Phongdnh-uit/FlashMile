package com.uit.se356.core.infrastructure.security.oauth2;

import com.uit.se356.core.application.authentication.command.OAuth2LoginCommand;
import com.uit.se356.core.application.authentication.handler.OAuth2LoginCommandHandler;
import com.uit.se356.core.domain.entities.authentication.User;
import com.uit.se356.core.infrastructure.security.CustomUserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Component
public class CustomOAuth2Service extends DefaultOAuth2UserService {
  private final OAuth2LoginCommandHandler oAuth2LoginCommandHandler;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String provider = userRequest.getClientRegistration().getRegistrationId();
    String providerUserId = oAuth2User.getName();
    String email = oAuth2User.getAttribute("email");
    String fullName = oAuth2User.getAttribute("name");
    String verifiedPhone = getVerifiedPhoneFromSession();

    // Tạo command và gọi handler để xử lý logic đăng nhập/đăng ký
    OAuth2LoginCommand command =
        new OAuth2LoginCommand(provider, providerUserId, email, fullName, verifiedPhone);
    User user = oAuth2LoginCommandHandler.handle(command);

    return CustomUserPrincipal.builder().id(user.getId()).build();
  }

  private String getVerifiedPhoneFromSession() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    HttpSession session = request.getSession(false);
    if (session != null) {
      String verifiedPhone = (String) session.getAttribute("verifiedPhone");
      session.invalidate(); // Xóa session sau khi lấy số điện thoại đã xác thực
      return verifiedPhone;
    }
    return null;
  }
}
