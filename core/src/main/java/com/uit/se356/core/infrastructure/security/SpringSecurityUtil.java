package com.uit.se356.core.infrastructure.security;

import com.uit.se356.common.security.UserPrincipal;
import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityUtil implements SecurityUtil<UserId>, AuditorAware<String> {

  private static final String ANONYMOUS_USER_PRINCIPAL_NAME = "anonymousUser";

  @Override
  public Optional<UserPrincipal<UserId>> getCurrentUserPrincipal() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || isAnonymous(authentication)) {
      return Optional.empty();
    }

    Object principal = authentication.getPrincipal();

    if (principal instanceof UserPrincipal<?> userPrincipal) {
      @SuppressWarnings("unchecked")
      UserPrincipal<UserId> castedPrincipal = (UserPrincipal<UserId>) userPrincipal;
      return Optional.of(castedPrincipal);
    }

    return Optional.empty();
  }

  @Override
  public boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // Check if authentication is present, actually authenticated, and not an anonymous user.
    return authentication != null
        && authentication.isAuthenticated()
        && !isAnonymous(authentication);
  }

  private boolean isAnonymous(Authentication authentication) {
    Object principal = authentication.getPrincipal();
    return ANONYMOUS_USER_PRINCIPAL_NAME.equals(principal);
  }

  @Override
  public Optional<String> getCurrentAuditor() {
    return getCurrentUserPrincipal().map(userPrincipal -> userPrincipal.getId().toString());
  }
}
