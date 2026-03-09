package com.uit.se356.core.application.area.command;

import com.uit.se356.common.dto.Command;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import org.springframework.web.multipart.MultipartFile;

public record ImportProvinceGeoJsonCommand(MultipartFile file) implements Command<Integer> {
  public ImportProvinceGeoJsonCommand {
    if (file == null || file.isEmpty()) {
      throw new AppException(CommonErrorCode.VALIDATION_ERROR, "GeoJSON file is required");
    }
  }
}
