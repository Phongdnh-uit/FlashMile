package com.uit.se356.core.presentation.rest.area;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.common.services.CommandBus;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.core.application.area.command.CreateProvinceCommand;
import com.uit.se356.core.application.area.projections.ProvinceSummaryProjection;
import com.uit.se356.core.application.area.query.ProvinceSummaryQuery;
import com.uit.se356.core.application.area.result.ProvinceResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Province Management", description = "API quản lý tỉnh/thành phố")
@RestController
@RequestMapping("/api/v1/admin/provinces")
@RequiredArgsConstructor
public class ProvinceController {

  private final CommandBus commandBus;
  private final QueryBus queryBus;

  @Operation(summary = "Create a new Province (Cấp Tỉnh)")
  @PostMapping
  public ResponseEntity<ApiResponse<ProvinceResult>> createProvince(
      @RequestBody CreateProvinceCommand command) {
    ProvinceResult result = commandBus.dispatch(command);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.created(result, "Province created successfully"));
  }

  @Operation(
      summary = "Get list of Provinces with pagination and filtering",
      description = "Hỗ trợ phân trang, sắp xếp và filter RSQL. Ví dụ: code==HCM;name==*Hồ*")
  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<ProvinceSummaryProjection>>> getAllProvinces(
      @ParameterObject @ModelAttribute SearchPageable pageable) {
    ProvinceSummaryQuery query = new ProvinceSummaryQuery(pageable);
    PageResponse<ProvinceSummaryProjection> result = queryBus.dispatch(query);
    return ResponseEntity.ok(ApiResponse.ok(result, "Provinces retrieved successfully"));
  }
}
