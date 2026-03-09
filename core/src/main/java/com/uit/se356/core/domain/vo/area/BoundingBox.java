package com.uit.se356.core.domain.vo.area;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AreaErrorCode;

public record BoundingBox(double minLat, double minLng, double maxLat, double maxLng) {
  public BoundingBox {
    if (minLat >= maxLat || minLng >= maxLng) {
      throw new AppException(
          AreaErrorCode.INVALID_BOUNDING_BOX, "Invalid Bounding Box coordinates.");
    }
    if (minLat < -90 || maxLat > 90 || minLng < -180 || maxLng > 180) {
      throw new AppException(
          AreaErrorCode.INVALID_BOUNDING_BOX, "Coordinates out of valid global range.");
    }
  }
}
