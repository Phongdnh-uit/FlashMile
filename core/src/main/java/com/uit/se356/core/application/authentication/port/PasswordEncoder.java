package com.uit.se356.core.application.authentication.port;

public interface PasswordEncoder {
  String encode(String rawPassword);

  boolean matches(String rawPassword, String encodedPassword);
}
