package com.uit.se356.core.presentation.rest.auth;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.core.application.authentication.command.mfa.CompleteSetupMfaCommand;
import com.uit.se356.core.application.authentication.command.mfa.InitiateMfaSetupCommand;
import com.uit.se356.core.application.authentication.command.mfa.MfaChallengeCommand;
import com.uit.se356.core.application.authentication.result.mfa.CompleteSetupMfaResult;
import com.uit.se356.core.application.authentication.result.mfa.MfaChallengeResult;
import com.uit.se356.core.domain.vo.authentication.MfaMethod;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  // Placeholder cho endpoint lấy danh sách đã cấu hình MFA
  @GetMapping("/methods")
  public ResponseEntity<ApiResponse<Void>> getMfaMethods() {
    throw new UnsupportedOperationException("Not implemented yet");
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
      @RequestBody CompleteSetupMfaCommand command) {
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
  public ResponseEntity<ApiResponse<Void>> verifyMfa() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @PostMapping("/recovery")
  public ResponseEntity<ApiResponse<Void>> recoverMfa() {
    throw new UnsupportedOperationException("Not implemented yet");
  }
}
