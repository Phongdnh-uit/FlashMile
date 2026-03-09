package com.uit.se356.core.domain.vo.area;

public enum WardType {
  WARD("Xã"),
  COMMUNE("Xã"),
  TOWNSHIP("Thị trấn"),
  TOWN("Thị");

  private final String displayName;

  WardType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
