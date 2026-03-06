package com.uit.se356.core.domain.entities.area;

import com.uit.se356.common.exception.AppException;
import com.uit.se356.core.domain.exception.AreaErrorCode;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import java.util.Objects;

public class Ward {
  private final String id;
  private String code;
  private String name;
  private String provinceId;
  private BoundingBox boundingBox;

  private Ward(String id, String code, String name, String provinceId, BoundingBox boundingBox) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.provinceId = provinceId;
    this.boundingBox = boundingBox;
  }

  // Factory method
  public static Ward createNewWard(
      String id, String code, String name, String provinceId, BoundingBox boundingBox) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(code);
    Objects.requireNonNull(name);
    Objects.requireNonNull(boundingBox);

    if (provinceId == null || provinceId.isBlank()) {
      throw new AppException(AreaErrorCode.MISSING_PROVINCE_ID);
    }

    return new Ward(id, code, name, provinceId, boundingBox);
  }

  public void update(String code, String name, String provinceId, BoundingBox boundingBox) {
    Objects.requireNonNull(code);
    Objects.requireNonNull(name);
    Objects.requireNonNull(boundingBox);

    if (provinceId == null || provinceId.isBlank()) {
      throw new AppException(AreaErrorCode.MISSING_PROVINCE_ID);
    }

    this.code = code;
    this.name = name;
    this.provinceId = provinceId;
    this.boundingBox = boundingBox;
  }

  public static Ward rehydrate(
      String id, String code, String name, String provinceId, BoundingBox boundingBox) {
    return new Ward(id, code, name, provinceId, boundingBox);
  }

  // ---------------------------- Getters ----------------------------
  public String getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getProvinceId() {
    return provinceId;
  }

  public BoundingBox getBoundingBox() {
    return boundingBox;
  }
}
