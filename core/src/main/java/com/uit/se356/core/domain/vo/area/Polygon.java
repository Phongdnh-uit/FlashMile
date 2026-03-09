package com.uit.se356.core.domain.vo.area;

import java.util.List;
import java.util.Objects;

public record Polygon(List<Coordinate> coordinates) {
  public Polygon {
    Objects.requireNonNull(coordinates, "Coordinates must not be null");
    if (coordinates.size() < 3) {
      throw new IllegalArgumentException("Polygon must have at least 3 coordinates");
    }
  }

  public record Coordinate(double latitude, double longitude) {
    public Coordinate {
      if (latitude < -90 || latitude > 90) {
        throw new IllegalArgumentException("Latitude must be between -90 and 90");
      }
      if (longitude < -180 || longitude > 180) {
        throw new IllegalArgumentException("Longitude must be between -180 and 180");
      }
    }
  }
}
