package com.uit.se356.core.presentation.rest.auth;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.core.application.authentication.command.RegisterCommand;
import com.uit.se356.core.application.authentication.handler.LoginQueryHandler;
import com.uit.se356.core.application.authentication.handler.ProcessVerificationHandler;
import com.uit.se356.core.application.authentication.handler.RegisterCommandHandler;
import com.uit.se356.core.application.authentication.handler.SendVerificationCodeHandler;
import com.uit.se356.core.application.authentication.query.LoginQuery;
import com.uit.se356.core.application.authentication.query.ProcessVerificationQuery;
import com.uit.se356.core.application.authentication.query.SendVerificationCodeQuery;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.application.authentication.result.RegisterResult;
import com.uit.se356.core.application.authentication.result.VerificationResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final LoginQueryHandler loginQueryHandler;
  private final SendVerificationCodeHandler sendVerificationCodeHandler;
  private final ProcessVerificationHandler processVerificationHandler;
  private final RegisterCommandHandler registerCommandHandler;

  @Operation(summary = "User Login")
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResult>> login(@RequestBody LoginQuery query) {
    LoginResult result = loginQueryHandler.handle(query);
    return ResponseEntity.ok(ApiResponse.ok(result, "Login successful"));
  }

  @Operation(summary = "Send Verification Code")
  @PostMapping("/send-verification")
  public ResponseEntity<ApiResponse<Void>> sendVerification(
      @RequestBody SendVerificationCodeQuery query) {
    sendVerificationCodeHandler.handle(query);
    return ResponseEntity.ok(ApiResponse.ok(null, "Verification code sent"));
  }

  @Operation(summary = "Verify Code")
  @PostMapping("/verify-code")
  public ResponseEntity<ApiResponse<VerificationResult>> verifyCode(
      @RequestBody ProcessVerificationQuery query) {
    VerificationResult result = processVerificationHandler.handle(query);
    return ResponseEntity.ok(ApiResponse.ok(result, "Verification successful"));
  }

  @Operation(summary = "Register User")
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<RegisterResult>> registerUser(
      @RequestBody RegisterCommand command) {
    return ResponseEntity.ok(
        ApiResponse.ok(registerCommandHandler.handle(command), "User registered successfully"));
  }
}
