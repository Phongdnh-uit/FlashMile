package com.uit.se356.core.application.area.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.core.application.area.command.ImportWardGeoJsonCommand;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.application.area.port.WardRepository;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.entities.area.Ward;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import com.uit.se356.core.infrastructure.utils.GeoJsonParserUtil;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

public class ImportWardGeoJsonHandler implements CommandHandler<ImportWardGeoJsonCommand, Integer> {

  private final ProvinceRepository provinceRepository;
  private final WardRepository wardRepository;
  private final ObjectMapper objectMapper;

  public ImportWardGeoJsonHandler(
      ProvinceRepository provinceRepository,
      WardRepository wardRepository,
      ObjectMapper objectMapper) {
    this.provinceRepository = provinceRepository;
    this.wardRepository = wardRepository;
    this.objectMapper = objectMapper;
  }

  @Override
  @Transactional
  public Integer handle(ImportWardGeoJsonCommand command) {
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

        String wardCode =
            properties.has("ma_xa")
                ? properties.get("ma_xa").asText()
                : properties.get("Code").asText();
        String wardName =
            properties.has("ten_xa")
                ? properties.get("ten_xa").asText()
                : properties.get("Name").asText();
        String provinceCode =
            properties.has("ma_tinh")
                ? properties.get("ma_tinh").asText()
                : properties.get("ProvinceCode").asText();

        // 1. Chỉ xử lý nếu Xã này chưa tồn tại trong Database
        if (!wardRepository.existsByCode(wardCode)) {

          // 2. Tra cứu Province ID từ Province Code
          Optional<Province> provinceOpt = provinceRepository.findByCode(provinceCode);

          if (provinceOpt.isPresent()) {
            String provinceId = provinceOpt.get().getId();

            // 3. Tính toán Bounding Box bằng Utility đã tạo
            BoundingBox bbox = GeoJsonParserUtil.calculateBoundingBox(geometry);

            // 4. Tạo và lưu Entity Xã
            Ward ward = Ward.createNewWard(wardCode, wardName, provinceId, bbox);
            wardRepository.create(ward);
            count++;

          } else {
            // Ghi tam log ra
            System.out.println(
                "Skipped Ward [{}] {} because Province Code [{}] is not found in DB: wardCode: "
                    + wardCode
                    + " wardName: "
                    + wardName
                    + " provinceCode: "
                    + provinceCode);
          }
        }
      }
    } catch (Exception e) {
      throw new AppException(
          CommonErrorCode.UNCATEGORIZED_EXCEPTION,
          "Failed to parse Ward GeoJSON file: " + e.getMessage());
    }

    return count; // Trả về số lượng Xã đã import thành công
  }
}
