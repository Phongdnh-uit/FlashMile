package com.uit.se356.core.application.area.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.area.command.ImportProvinceGeoJsonCommand;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import com.uit.se356.core.infrastructure.utils.GeoJsonParserUtil;
import org.springframework.transaction.annotation.Transactional;

public class ImportProvinceGeoJsonHandler
    implements CommandHandler<ImportProvinceGeoJsonCommand, Integer> {

  private final ProvinceRepository provinceRepository;
  private ObjectMapper objectMapper;

  public ImportProvinceGeoJsonHandler(ProvinceRepository provinceRepository) {
    this.provinceRepository = provinceRepository;
  }

  @Override
  @Transactional
  public Integer handle(ImportProvinceGeoJsonCommand command) {
    int count = 0;
    try {
      JsonNode rootNode = objectMapper.readTree(command.file().getInputStream());
      JsonNode features = rootNode.get("features");

      if (features == null || !features.isArray()) {
        throw new AppException(CommonErrorCode.VALIDATION_ERROR, "Invalid GeoJSON format");
      }

      for (JsonNode feature : features) {
        JsonNode properties = feature.get("properties");
        JsonNode geometry = feature.get("geometry");

        String name =
            properties.has("ten_tinh")
                ? properties.get("ten_tinh").asText()
                : properties.get("Name").asText();
        String code =
            properties.has("ma_tinh")
                ? properties.get("ma_tinh").asText()
                : properties.get("Code").asText();

        BoundingBox bbox = GeoJsonParserUtil.calculateBoundingBox(geometry);

        if (!provinceRepository.existsByCode(code)) {
          Province province = Province.createNewProvince(code, name, bbox);
          provinceRepository.create(province);
          count++;
        }
      }
    } catch (Exception e) {
      throw new AppException(
          CommonErrorCode.UNCATEGORIZED_EXCEPTION,
          "Failed to parse GeoJSON file: " + e.getMessage());
    }

    // Trả về số lượng Tỉnh đã import thành công
    return count;
  }
}
