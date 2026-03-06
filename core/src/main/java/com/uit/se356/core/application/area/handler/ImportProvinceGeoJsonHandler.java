package com.uit.se356.core.application.area.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.area.command.ImportProvinceGeoJsonCommand;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import com.uit.se356.core.infrastructure.utils.GeoJsonParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class ImportProvinceGeoJsonHandler
    implements CommandHandler<ImportProvinceGeoJsonCommand, Integer> {

  private final ProvinceRepository provinceRepository;
  private final ObjectMapper objectMapper;
  private final IdGenerator idGenerator;

  public ImportProvinceGeoJsonHandler(
      ProvinceRepository provinceRepository, ObjectMapper objectMapper, IdGenerator idGenerator) {
    this.provinceRepository = provinceRepository;
    this.objectMapper = objectMapper;
    this.idGenerator = idGenerator;
  }

  @Override
  @Transactional
  public Integer handle(ImportProvinceGeoJsonCommand command) {
    int count = 0;
    try {
      JsonNode rootNode = objectMapper.readTree(command.file().getInputStream());
      JsonNode features = rootNode.path("features");

      if (features.isMissingNode() || !features.isArray()) {
        throw new AppException(CommonErrorCode.VALIDATION_ERROR, "Invalid GeoJSON format");
      }

      for (JsonNode feature : features) {
        try {
          JsonNode properties = feature.path("properties");
          JsonNode geometry = feature.path("geometry");

          if ("Point".equalsIgnoreCase(geometry.path("type").asText(""))) continue;

          // CẬP NHẬT: Quét các key phù hợp với file gis.vn
          String name = extractProperty(properties, "ten_tinh", "name", "TinhThanh");
          String code = extractProperty(properties, "ma_tinh", "ISO3166-2", "Ma");

          if (code.isBlank()) {
            code = extractProperty(properties, "@id", "OBJECTID");
          }

          if (name.isBlank() || code.isBlank()) continue;

          BoundingBox bbox = GeoJsonParserUtil.calculateBoundingBox(geometry);

          if (!provinceRepository.existsByCode(code)) {
            String newId = idGenerator.generate().toString();
            Province province = Province.create(newId, code, name, bbox);

            provinceRepository.create(province);
            count++;
          }
        } catch (Exception ex) {
          log.error("Lỗi khi xử lý tỉnh [{}]: {}", feature.path("properties").toString(), ex.getMessage());
        }
      }
    } catch (Exception e) {
      throw new AppException(CommonErrorCode.UNCATEGORIZED_EXCEPTION, "Failed to parse GeoJSON: " + e.getMessage());
    }

    return count;
  }

  private String extractProperty(JsonNode properties, String... possibleKeys) {
    for (String key : possibleKeys) {
      if (properties.hasNonNull(key)) {
        return properties.get(key).asText().trim();
      }
    }
    return "";
  }
}