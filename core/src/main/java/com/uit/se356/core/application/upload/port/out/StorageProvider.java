package com.uit.se356.core.application.upload.port.out;

import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import java.time.Duration;

public interface StorageProvider {

  PresignedUrlResult generatePutPresignedUrl(String storageKey, Duration expiration);

  PresignedUrlResult generateGetPresignedUrl(String storageKey, Duration expiration);

  void delete(String storageKey);

  void promoteFromQuarantine(String storageKey);
}
