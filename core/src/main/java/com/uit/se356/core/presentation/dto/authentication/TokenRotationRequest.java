package com.uit.se356.core.presentation.dto.authentication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRotationRequest {
  private String refreshToken;
}
