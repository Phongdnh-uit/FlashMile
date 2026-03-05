package com.uit.se356.core.infrastructure.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.uit.se356.core.domain.vo.area.BoundingBox;

public class GeoJsonParserUtil {

  /** Tính toán BoundingBox từ JsonNode Geometry của GeoJSON */
  public static BoundingBox calculateBoundingBox(JsonNode geometryNode) {
    // Mảng lưu trữ: [minLng, maxLng, minLat, maxLat]
    double[] bounds = {Double.MAX_VALUE, -Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE};

    JsonNode coordinatesNode = geometryNode.get("coordinates");
    extractCoordinates(coordinatesNode, bounds);

    // Trả về BoundingBox(minLat, minLng, maxLat, maxLng)
    return new BoundingBox(bounds[2], bounds[0], bounds[3], bounds[1]);
  }

  /** Hàm đệ quy để đào sâu vào các mảng tọa độ */
  private static void extractCoordinates(JsonNode node, double[] bounds) {
    if (node.isArray()) {
      if (node.get(0).isNumber()) {
        // Cấu trúc của 1 điểm luôn là [Longitude, Latitude] trong GeoJSON
        double lng = node.get(0).asDouble();
        double lat = node.get(1).asDouble();

        if (lng < bounds[0]) bounds[0] = lng; // Cực Tây
        if (lng > bounds[1]) bounds[1] = lng; // Cực Đông
        if (lat < bounds[2]) bounds[2] = lat; // Cực Nam
        if (lat > bounds[3]) bounds[3] = lat; // Cực Bắc
      } else {
        // Đệ quy chui vào mảng con
        for (JsonNode child : node) {
          extractCoordinates(child, bounds);
        }
      }
    }
  }
}
