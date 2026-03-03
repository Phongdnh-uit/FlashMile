package com.uit.se356.core.application.upload.strategies;

import com.uit.se356.core.application.upload.command.UploadPresignedUrlCommand;
import com.uit.se356.core.domain.vo.upload.UploadType;

public interface UploadPolicy {
  boolean validate(UploadPresignedUrlCommand command);

  UploadType type();
}
