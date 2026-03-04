package com.uit.se356.core.application.upload.port.out;

import com.uit.se356.core.application.upload.query.PutPresignedUrlQuery;
import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import com.uit.se356.core.application.upload.result.StoredObjectMetadata;
import java.time.Duration;
import java.util.List;

public interface StorageProvider {

  PresignedUrlResult generatePutPresignedUrl(PutPresignedUrlQuery query);

  PresignedUrlResult generateGetPresignedUrl(String storageKey, Duration expiration);

  void delete(String storageKey);

  void deleteAll(List<String> storageKeys);

  void promoteFromQuarantine(String storageKey);

  StoredObjectMetadata getObjectStat(String storageKey);

  StoredObjectMetadata getQuarantineObjectStat(String storageKey);
}
