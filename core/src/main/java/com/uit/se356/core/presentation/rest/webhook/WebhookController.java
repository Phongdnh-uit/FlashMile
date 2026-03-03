package com.uit.se356.core.presentation.rest.webhook;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.core.application.upload.command.ConfirmUploadCommand;
import com.uit.se356.core.presentation.dto.upload.MinioWebhookRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Webhooks")
@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

  private final CommandBus commandBus;

  @Operation(summary = "Handle Minio upload notification")
  @PostMapping("/minio")
  public ResponseEntity<ApiResponse<Void>> handleMinioWebhook(
      @RequestBody MinioWebhookRequest request) {
    log.info("Received Minio webhook: {}", request);
    if (request.Records() == null || request.Records().isEmpty()) {
      return ResponseEntity.ok(ApiResponse.ok(null, "No records found in webhook"));
    }

    request.Records()
        .forEach(
            record -> {
              var object = record.s3().object();
              var command =
                  new ConfirmUploadCommand(object.key(), object.size(), object.contentType());
              commandBus.dispatch(command);
            });

    return ResponseEntity.ok(ApiResponse.ok(null, "Webhook processed successfully"));
  }
}
