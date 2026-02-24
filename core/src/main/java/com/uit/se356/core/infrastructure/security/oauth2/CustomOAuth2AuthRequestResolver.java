package com.uit.se356.core.infrastructure.security.oauth2;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.constants.CacheKey;
import com.uit.se356.core.domain.constants.SystemConstant;
import com.uit.se356.core.domain.exception.AuthErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AuthRequestResolver implements OAuth2AuthorizationRequestResolver {

  private final StringRedisTemplate redisTemplate;

  private final ClientRegistrationRepository clientRegistrationRepository;

  private final DefaultOAuth2AuthorizationRequestResolver defaultResolver;

  public CustomOAuth2AuthRequestResolver(
      StringRedisTemplate redisTemplate,
      ClientRegistrationRepository clientRegistrationRepository) {
    this.redisTemplate = redisTemplate;
    this.clientRegistrationRepository = clientRegistrationRepository;
    this.defaultResolver =
        new DefaultOAuth2AuthorizationRequestResolver(
            this.clientRegistrationRepository, SystemConstant.OAUTH2_AUTHORIZATION_BASE_URI);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
    OAuth2AuthorizationRequest base = defaultResolver.resolve(request);
    return customizeAuthorizationRequest(request, base);
  }

  @Override
  public OAuth2AuthorizationRequest resolve(
      HttpServletRequest request, String clientRegistrationId) {
    OAuth2AuthorizationRequest base = defaultResolver.resolve(request, clientRegistrationId);
    return customizeAuthorizationRequest(request, base);
  }

  private OAuth2AuthorizationRequest customizeAuthorizationRequest(
      HttpServletRequest request, OAuth2AuthorizationRequest base) {
    if (base == null) return null;

    // Chỉ xử lý cho đăng ký tài khoản mới
    String token = request.getParameter("verificationToken");
    if (token != null) {
      String key = CacheKey.PHONE_VERIFIED_PREFIX + ":" + token;
      String cachedPhone = redisTemplate.opsForValue().get(key);
      if (cachedPhone == null) {
        throw new AppException(AuthErrorCode.INVALID_VERIFICATION_CODE);
      }
      // Lưu số điện thoại đã xác minh vào session để sử dụng trong quá trình đăng ký
      request.getSession(true).setAttribute("verifiedPhone", cachedPhone);
    }

    return base;
  }
}
