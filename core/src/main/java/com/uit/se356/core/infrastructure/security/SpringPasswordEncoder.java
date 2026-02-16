package com.uit.se356.core.infrastructure.security;

import com.uit.se356.core.application.authentication.port.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SpringPasswordEncoder implements PasswordEncoder {
  private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

  @Override
  public String encode(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  @Override
  public boolean matches(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }
}
