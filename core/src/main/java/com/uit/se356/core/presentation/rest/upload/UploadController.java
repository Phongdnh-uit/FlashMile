package com.uit.se356.core.presentation.rest.upload;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.core.application.upload.command.UploadPresignedUrlCommand;
import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Upload")
@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController {

  private final CommandBus commandBus;

  @Operation(summary = "Get a presigned URL for file upload")
  @PostMapping("/presigned-url")
  public ResponseEntity<ApiResponse<PresignedUrlResult>> getPresignedUrl(
      @RequestBody UploadPresignedUrlCommand command) {
    PresignedUrlResult result = commandBus.dispatch(command);
    return ResponseEntity.ok(ApiResponse.ok(result, "Presigned URL generated successfully"));
  }
}
