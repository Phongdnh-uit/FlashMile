package com.uit.se356.core.infrastructure.storage;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.upload.port.out.StorageProvider;
import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import com.uit.se356.core.infrastructure.config.AppProperties;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MinIOStorageProvider implements StorageProvider {
  private final MinioClient minioClient;
  private final AppProperties appProperties;

  @Override
  public PresignedUrlResult generatePutPresignedUrl(String storageKey, Duration expiration) {
    // 1. Format lại storageKey
    storageKey = normalizeStorageKey(storageKey);
    try {
      String url =
          minioClient.getPresignedObjectUrl(
              GetPresignedObjectUrlArgs.builder()
                  // Mới upload chỉ thêm nó vào quarantine thôi, sau đó sẽ có một process khác di
                  // chuyển nó vào đúng bucket
                  .bucket(appProperties.getS3().getQuarantineBucketName())
                  .object(storageKey)
                  .method(Method.PUT)
                  .expiry((int) expiration.toSeconds(), TimeUnit.SECONDS)
                  .build());
      return new PresignedUrlResult(url, storageKey, expiration.getSeconds());
    } catch (Exception e) {
      log.error("Failed to generate presigned URL for key {}: {}", storageKey, e.getMessage());
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  public PresignedUrlResult generateGetPresignedUrl(String storageKey, Duration expiration) {
    try {
      String url =
          minioClient.getPresignedObjectUrl(
              GetPresignedObjectUrlArgs.builder()
                  // Lấy file từ bucket chính, vì sau khi upload xong sẽ có một process khác di
                  // chuyển file từ quarantine vào đúng bucket
                  .bucket(appProperties.getS3().getMainBucketName())
                  .object(storageKey)
                  .method(Method.GET)
                  .expiry((int) expiration.toSeconds(), TimeUnit.SECONDS)
                  .build());
      return new PresignedUrlResult(url, storageKey, expiration.getSeconds());
    } catch (Exception e) {
      log.error("Failed to generate GET presigned URL for key {}: {}", storageKey, e.getMessage());
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  public void delete(String storageKey) {
    try {
      minioClient.removeObject(RemoveObjectArgs.builder().object(storageKey).build());
    } catch (Exception e) {
      log.error("Failed to delete object with key {}: {}", storageKey, e.getMessage());
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }

  @Override
  public void promoteFromQuarantine(String storageKey) {
    try {
      minioClient.copyObject(
          CopyObjectArgs.builder()
              .source(
                  CopySource.builder()
                      .bucket(appProperties.getS3().getQuarantineBucketName())
                      .object(storageKey)
                      .build())
              .bucket(appProperties.getS3().getMainBucketName())
              .object(storageKey)
              .build());
      // Sau khi copy xong thì xóa file trong quarantine
      minioClient.removeObject(
          RemoveObjectArgs.builder()
              .bucket(appProperties.getS3().getQuarantineBucketName())
              .object(storageKey)
              .build());
    } catch (Exception e) {
      log.error(
          "Failed to promote object from quarantine with key {}: {}", storageKey, e.getMessage());
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }

  // ============================ HELPERS ============================
  private String normalizeStorageKey(String key) {
    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("Invalid storage key");
    }

    key = key.trim();

    // Chuẩn hóa separator
    key = key.replace("\\", "/");

    // Remove leading slash
    key = key.replaceAll("^/+", "");

    // Remove duplicate slashes
    key = key.replaceAll("/{2,}", "/");

    // Chặn path traversal
    if (key.contains("..")) {
      throw new IllegalArgumentException("Path traversal detected");
    }

    // Giới hạn ký tự hợp lệ
    if (!key.matches("^[a-zA-Z0-9._/-]+$")) {
      throw new IllegalArgumentException("Invalid characters in storage key");
    }

    return key;
  }
}
