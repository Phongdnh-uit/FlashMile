package com.uit.se356.core.application.upload.strategies.upload;

import com.uit.se356.core.application.upload.command.UploadPresignedUrlCommand;
import com.uit.se356.core.domain.vo.upload.UploadType;

public interface UploadPolicy {
  void validate(UploadPresignedUrlCommand command);

  UploadType type();
}
