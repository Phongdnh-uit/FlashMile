package com.uit.se356.core.domain.vo.depot;

public record DepotId(String value) {
  public DepotId {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Depot ID cannot be null or blank");
    }
  }
}
