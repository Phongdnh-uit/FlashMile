package com.uit.se356.core.infrastructure.security;

import com.uit.se356.common.security.UserPrincipal;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
@Getter
public class CustomUserPrincipal implements UserPrincipal<UserId>, OAuth2User {

  private final UserId id;
  private final String role;
  private final Map<String, Object> attributes;

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getName() {
    return id.value();
  }
}
