package com.uit.se356.core.presentation.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRotationRequest {
  @NotBlank private String refreshToken;
}
