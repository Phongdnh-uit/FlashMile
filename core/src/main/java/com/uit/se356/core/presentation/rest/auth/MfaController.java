package com.uit.se356.core.presentation.rest.auth;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.core.application.authentication.command.mfa.CompleteSetupMfaCommand;
import com.uit.se356.core.application.authentication.command.mfa.InitiateMfaSetupCommand;
import com.uit.se356.core.application.authentication.command.mfa.MfaChallengeCommand;
import com.uit.se356.core.application.authentication.command.mfa.RecoveryMfaCommand;
import com.uit.se356.core.application.authentication.command.mfa.RemoveMfaMethodCommand;
import com.uit.se356.core.application.authentication.command.mfa.VerifyMfaCommand;
import com.uit.se356.core.application.authentication.projections.MfaMethodProjection;
import com.uit.se356.core.application.authentication.query.mfa.GetActiveMethodsQuery;
import com.uit.se356.core.application.authentication.result.LoginResult;
import com.uit.se356.core.application.authentication.result.mfa.CompleteSetupMfaResult;
import com.uit.se356.core.application.authentication.result.mfa.MfaChallengeResult;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import com.uit.se356.core.infrastructure.config.AppProperties;
import com.uit.se356.core.presentation.request.mfa.CompleteWebAuthnSetupRequest;
import com.uit.se356.core.presentation.request.mfa.VerifyWebAuthnRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MFA")
@RequestMapping("/api/v1/auth/mfa")
@RequiredArgsConstructor
@RestController
public class MfaController {
  private final CommandBus commandBus;
  private final QueryBus queryBus;
  private final AppProperties appProperties;

  @GetMapping("/methods")
  public ResponseEntity<ApiResponse<List<MfaMethodProjection>>> getMfaMethods() {
    GetActiveMethodsQuery query = new GetActiveMethodsQuery();
    return ResponseEntity.ok(
        ApiResponse.ok(queryBus.dispatch(query), "Active MFA methods retrieved successfully"));
  }

  @Operation(summary = "Khởi tạo quá trình thiết lập MFA cho người dùng")
  @PostMapping("/setup/init")
  public ResponseEntity<ApiResponse<Map<String, String>>> initiateMfaSetup(
      @RequestParam("method") MfaMethod method) {
    InitiateMfaSetupCommand command = new InitiateMfaSetupCommand(method);
    return ResponseEntity.ok(
        ApiResponse.ok(commandBus.dispatch(command), "MFA setup initiated successfully"));
  }

  @Operation(summary = "Hoàn tất quá trình thiết lập MFA cho người dùng")
  @PostMapping("/setup/complete")
  public ResponseEntity<ApiResponse<CompleteSetupMfaResult>> completeMfaSetup(
      @RequestParam("method") MfaMethod method,
      @RequestBody CompleteWebAuthnSetupRequest request) { // Use new DTO
    CompleteSetupMfaCommand command = new CompleteSetupMfaCommand(
        method,
        null, // Credential is null for WebAuthn
        Map.of(
            "attestationObject", request.getAttestationObject(),
            "clientDataJSON", request.getClientDataJSON()
        )
    );
    return ResponseEntity.ok(
        ApiResponse.ok(commandBus.dispatch(command), "MFA setup completed successfully"));
  }

  @Operation(summary = "Thực hiện MFA challenge")
  @PostMapping("/challenge")
  public ResponseEntity<ApiResponse<MfaChallengeResult>> challengeMfa(
      @RequestBody MfaChallengeCommand command) {
    return ResponseEntity.ok(
        ApiResponse.ok(commandBus.dispatch(command), "MFA challenge successful"));
  }

  @PostMapping("/verify")
  public ResponseEntity<ApiResponse<LoginResult>> verifyMfa(
      @RequestParam("method") MfaMethod method,
      @RequestBody VerifyWebAuthnRequest request) { // Use new DTO
    VerifyMfaCommand command = new VerifyMfaCommand(
        request.getChallengeId(),
        null, // Code is null for WebAuthn
        Map.of(
            "credentialId", request.getCredentialId(),
            "authenticatorData", request.getAuthenticatorData(),
            "clientDataJSON", request.getClientDataJSON(),
            "signature", request.getSignature()
        )
    );
    LoginResult result = commandBus.dispatch(command);
    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", result.refreshToken())
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofMillis(appProperties.getSecurity().getJwt().getRefreshExpiration()))
            .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponse.ok(result, "MFA verification successful"));
  }

  @PostMapping("/recovery")
  public ResponseEntity<ApiResponse<LoginResult>> recoverMfa(
      @RequestBody RecoveryMfaCommand command) {
    LoginResult result = commandBus.dispatch(command);
    ResponseCookie cookie =
        ResponseCookie.from("refreshToken", result.refreshToken())
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Duration.ofMillis(appProperties.getSecurity().getJwt().getRefreshExpiration()))
            .build();
    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(ApiResponse.ok(commandBus.dispatch(command), "MFA recovery successful"));
  }

  @DeleteMapping("/remove/{method}")
  public ResponseEntity<ApiResponse<Void>> removeMfaMethod(
      @PathVariable("method") MfaMethod method) {
    RemoveMfaMethodCommand command = new RemoveMfaMethodCommand(method);
    return ResponseEntity.ok(
        ApiResponse.ok(commandBus.dispatch(command), "MFA method removed successfully"));
  }
}
