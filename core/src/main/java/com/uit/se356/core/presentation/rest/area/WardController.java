package com.uit.se356.core.presentation.rest.area;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.core.application.area.command.CreateWardCommand;
import com.uit.se356.core.application.area.result.WardResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Ward Management")
@RestController
@RequestMapping("/api/v1/admin/wards")
@RequiredArgsConstructor
public class WardController {

  private final CommandBus commandBus;

  @Operation(summary = "Create a new Ward (Cấp Xã)")
  //  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<ApiResponse<WardResult>> createWard(
      @RequestBody CreateWardCommand command) {

    WardResult result = commandBus.dispatch(command);
    return ResponseEntity.ok(ApiResponse.created(result, "Ward created successfully"));
  }
}
