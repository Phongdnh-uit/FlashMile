package com.uit.se356.core.presentation.web;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.core.application.authentication.handler.LoginQueryHandler;
import com.uit.se356.core.application.authentication.query.LoginQuery;
import com.uit.se356.core.application.authentication.result.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final LoginQueryHandler loginQueryHandler;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResult>> login(@RequestBody LoginQuery query) {
    LoginResult result = loginQueryHandler.handle(query);
    return ResponseEntity.ok(ApiResponse.ok(result, "Login successful"));
  }
}
