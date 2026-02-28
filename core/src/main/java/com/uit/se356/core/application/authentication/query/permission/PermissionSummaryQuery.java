package com.uit.se356.core.application.authentication.query.permission;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.Query;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.application.authentication.projections.PermissionSummaryProjection;

public record PermissionSummaryQuery(SearchPageable pageable)
    implements Query<PageResponse<PermissionSummaryProjection>> {}
