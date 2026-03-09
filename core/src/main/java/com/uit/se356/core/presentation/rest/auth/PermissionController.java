package com.uit.se356.core.presentation.rest.auth;

import com.uit.se356.common.dto.ApiResponse;
import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.common.services.QueryBus;
import com.uit.se356.core.application.authentication.projections.PermissionSummaryProjection;
import com.uit.se356.core.application.authentication.query.permission.PermissionSummaryQuery;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Permission")
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
@RestController
public class PermissionController {

  private final QueryBus queryBus;

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<PermissionSummaryProjection>>> getAllPermissions(
      @ParameterObject @ModelAttribute SearchPageable pageable) {
    return ResponseEntity.ok(
        ApiResponse.ok(
            queryBus.dispatch(new PermissionSummaryQuery(pageable)),
            "Permissions retrieved successfully"));
  }
}
