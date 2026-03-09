package com.uit.se356.core.application.upload.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.core.application.upload.result.PresignedUrlResult;
import com.uit.se356.core.domain.vo.upload.FileId;

public record GetPresignedUrlQuery(FileId fileId) implements Query<PresignedUrlResult> {
  public GetPresignedUrlQuery {
    if (fileId == null) {
      throw new AppException(CommonErrorCode.INVALID_ID_FORMAT);
    }
  }
}
