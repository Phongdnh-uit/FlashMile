package com.uit.se356.core.infrastructure.security;

import com.uit.se356.common.security.UserPrincipal;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.Collection;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
@Getter
public class CustomUserPrincipal implements UserPrincipal<UserId>, OAuth2User {

  private final UserId id;

  @Override
  public Map<String, Object> getAttributes() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAttributes'");
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");
  }

  @Override
  public String getName() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getName'");
  }
}
