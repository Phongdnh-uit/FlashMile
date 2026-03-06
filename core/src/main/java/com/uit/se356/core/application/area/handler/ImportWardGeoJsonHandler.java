package com.uit.se356.core.application.area.handler;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uit.se356.common.exception.AppException;
import com.uit.se356.common.exception.CommonErrorCode;
import com.uit.se356.common.services.CommandHandler;
import com.uit.se356.common.utils.IdGenerator;
import com.uit.se356.core.application.area.command.ImportWardGeoJsonCommand;
import com.uit.se356.core.application.area.port.ProvinceRepository;
import com.uit.se356.core.application.area.port.WardRepository;
import com.uit.se356.core.domain.entities.area.Province;
import com.uit.se356.core.domain.entities.area.Ward;
import com.uit.se356.core.domain.vo.area.BoundingBox;
import com.uit.se356.core.infrastructure.utils.GeoJsonParserUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class ImportWardGeoJsonHandler implements CommandHandler<ImportWardGeoJsonCommand, Integer> {

  private final ProvinceRepository provinceRepository;
  private final WardRepository wardRepository;
  private final ObjectMapper objectMapper;
  private final IdGenerator idGenerator; // Bổ sung IdGenerator

  public ImportWardGeoJsonHandler(
      ProvinceRepository provinceRepository,
      WardRepository wardRepository,
      ObjectMapper objectMapper,
      IdGenerator idGenerator) { // Inject vào đây
    this.provinceRepository = provinceRepository;
    this.wardRepository = wardRepository;
    this.objectMapper = objectMapper;
    this.idGenerator = idGenerator;
  }

  @Override
  @Transactional
  public Integer handle(ImportWardGeoJsonCommand command) {
    int count = 0;
    JsonFactory factory = new JsonFactory();

    try (JsonParser parser = factory.createParser(command.file().getInputStream())) {
      // Di chuyển con trỏ đến mảng "features"
      while (parser.nextToken() != null) {
        if ("features".equals(parser.getCurrentName())) {
          break;
        }
      }

      if (parser.nextToken() != JsonToken.START_ARRAY) {
        throw new AppException(CommonErrorCode.VALIDATION_ERROR, "Không tìm thấy mảng features");
      }

      // Đọc từng Feature một (Streaming)
      while (parser.nextToken() == JsonToken.START_OBJECT) {
        try {
          JsonNode feature = objectMapper.readTree(parser); // Đọc 1 feature vào RAM
          JsonNode properties = feature.path("properties");
          JsonNode geometry = feature.path("geometry");

          // Bỏ qua Point
          if ("Point".equalsIgnoreCase(geometry.path("type").asText(""))) continue;

          // Lấy dữ liệu theo key từ file gis.vn
          String wardCode = properties.path("ma_xa").asText().trim();
          String wardName = properties.path("ten_xa").asText().trim();
          String provinceCode = properties.path("ma_tinh").asText().trim();

          if (wardCode.isEmpty() || wardName.isEmpty()) continue;

          if (!wardRepository.existsByCode(wardCode)) {
            Optional<Province> provinceOpt = provinceRepository.findByCode(provinceCode);

            if (provinceOpt.isPresent()) {
              BoundingBox bbox = GeoJsonParserUtil.calculateBoundingBox(geometry);

              // TẠO ID MỚI TRUYỀN VÀO HÀM CREATE ĐỂ TRÁNH LỖI IDENTIFIER PERSIST
              String newId = idGenerator.generate().toString();

              // Giả định bạn đã đổi Ward.createNewWard thành Ward.create(id, code, name...)
              Ward ward = Ward.createNewWard(newId, wardCode, wardName, provinceOpt.get().getId(), bbox);

              wardRepository.create(ward);
              count++;

            } else {
              log.warn("Bỏ qua Xã [{}] vì không tìm thấy mã Tỉnh [{}]", wardName, provinceCode);
            }
          }
        } catch (Exception ex) {
          log.error("Lỗi 1 feature phường/xã: {}", ex.getMessage());
        }
      }
    } catch (Exception e) {
      throw new AppException(CommonErrorCode.UNCATEGORIZED_EXCEPTION, "Lỗi đọc file Streaming: " + e.getMessage());
    }
    return count;
  }
}