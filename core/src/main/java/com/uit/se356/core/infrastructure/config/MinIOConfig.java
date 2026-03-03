package com.uit.se356.core.infrastructure.config;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MinIOConfig {

  private final AppProperties appProperties;

  @Bean
  MinioClient minioClient() {
    MinioClient minioClient =
        MinioClient.builder()
            .endpoint(appProperties.getS3().getEndpoint())
            .credentials(appProperties.getS3().getAccessKey(), appProperties.getS3().getSecretKey())
            .build();
    createBucketIfNotExists(minioClient, appProperties.getS3().getMainBucketName());
    createBucketIfNotExists(minioClient, appProperties.getS3().getQuarantineBucketName());
    return minioClient;
  }

  private void createBucketIfNotExists(MinioClient minioClient, String bucketName) {
    try {
      boolean found =
          minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
      if (!found) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
      }
    } catch (Exception e) {
      log.error("Error while creating bucket {}: {}", bucketName, e.getMessage());
      throw new AppException(CommonErrorCode.INTERNAL_ERROR);
    }
  }
}
