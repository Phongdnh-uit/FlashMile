package com.uit.se356.core.application.authentication.port.out;

public interface PasswordEncoder {
  String encode(String rawPassword);

  boolean matches(String rawPassword, String encodedPassword);
}
