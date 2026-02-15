package com.uit.se356.core.presentation.rest;

import com.uit.se356.common.utils.SecurityUtil;
import com.uit.se356.core.domain.vo.authentication.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DummyController {
  private final SecurityUtil<UserId> securityUtil;

  @GetMapping("/dummy")
  public String getDummy() {
    UserId userId = securityUtil.getCurrentUserPrincipal().get().getId();
    return "Dummy response for user: " + userId.value();
  }
}
