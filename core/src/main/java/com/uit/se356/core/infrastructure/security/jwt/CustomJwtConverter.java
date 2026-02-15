package com.uit.se356.core.infrastructure.security.jwt;

import com.uit.se356.common.security.UserPrincipal;
import com.uit.se356.core.application.user.port.UserRepository;
import com.uit.se356.core.domain.vo.authentication.UserId;
import com.uit.se356.core.infrastructure.security.CustomUserPrincipal;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

  private final UserRepository userRepository;

  @Override
  public AbstractAuthenticationToken convert(Jwt jwt) {
    // Collection<? extends GrantedAuthority> jwtAuthorities =
    //     jwtGrantedAuthoritiesConverter.convert(jwt);
    // Set<? extends GrantedAuthority> authorities =
    //     jwtAuthorities == null
    //         ? Collections.emptySet()
    //         : Collections.unmodifiableSet(Set.copyOf(jwtAuthorities));

    UserId userId = new UserId(jwt.getSubject());
    UserPrincipal<UserId> principal = CustomUserPrincipal.builder().id(userId).build();
    return new UsernamePasswordAuthenticationToken(principal, jwt, Set.of());
  }
}
