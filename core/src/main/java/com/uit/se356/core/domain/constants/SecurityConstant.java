package com.uit.se356.core.domain.constants;

public interface SecurityConstant {
  String[] PUBLIC_URLS = {"/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**"};
  String[] PUBLIC_GET_URLS = {};
}
