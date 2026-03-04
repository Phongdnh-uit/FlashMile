package com.uit.se356.core.application.upload.handler;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.QueryHandler;
import com.uit.se356.core.application.upload.port.out.FileRepository;
import com.uit.se356.core.application.upload.port.out.StorageProvider;
import com.uit.se356.core.application.upload.projections.BasicFileProjection;
import com.uit.se356.core.application.upload.query.GetPresignedUrlQuery;
import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import java.time.Duration;

public class GetPresignedUrlHandler
    implements QueryHandler<GetPresignedUrlQuery, PresignedUrlResult> {
  private final StorageProvider storageProvider;
  private final FileRepository fileRepository;

  public GetPresignedUrlHandler(StorageProvider storageProvider, FileRepository fileRepository) {
    this.storageProvider = storageProvider;
    this.fileRepository = fileRepository;
  }

  @Override
  public PresignedUrlResult handle(GetPresignedUrlQuery query) {
    // 1. Tìm file trong database
    BasicFileProjection file =
        fileRepository
            .findBasicById(query.fileId())
            .orElseThrow(() -> new AppException(CommonErrorCode.RESOURCE_NOT_FOUND));
    return storageProvider.generateGetPresignedUrl(file.getStorageKey(), Duration.ofMinutes(5));
  }
}
