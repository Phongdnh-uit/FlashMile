package com.uit.se356.core.presentation.dto.upload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MinioWebhookRequest(List<MinioRecord> Records) {
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record MinioRecord(S3 s3) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record S3(MinioObject object) {}

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record MinioObject(String key, long size, String contentType) {}
}
