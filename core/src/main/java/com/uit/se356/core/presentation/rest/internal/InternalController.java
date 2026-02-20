package com.uit.se356.core.presentation.rest.internal;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.core.application.internal.command.SyncPermissionCommand;
import com.uit.se356.core.application.internal.query.DebugOtpQuery;
import com.uit.se356.core.application.internal.result.DebugOtpResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Tag(name = "Internal")
@RequestMapping("/internal")
@RestController
public class InternalController {
  private final QueryBus queryBus;
  private final CommandBus commandBus;

  @PostMapping("/permissions/sync")
  public ResponseEntity<ApiResponse<Void>> syncPermissions(
      @RequestBody SyncPermissionCommand command) {
    commandBus.dispatch(command);
    return ResponseEntity.ok(ApiResponse.noContent("Permissions synchronized successfully"));
  }

  @GetMapping("/debug-otp")
  public ResponseEntity<ApiResponse<DebugOtpResult>> debugOtp(
      @RequestParam("phoneNumber") String phoneNumber) {
    DebugOtpQuery query = new DebugOtpQuery(phoneNumber);
    return ResponseEntity.ok(
        ApiResponse.ok(queryBus.dispatch(query), "OTP debug information retrieved successfully"));
  }
}
