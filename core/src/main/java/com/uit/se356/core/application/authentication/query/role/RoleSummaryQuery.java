package com.uit.se356.core.application.authentication.query.role;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.Query;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.application.authentication.projections.RoleSummaryProjection;

public record RoleSummaryQuery(SearchPageable pageable)
    implements Query<PageResponse<RoleSummaryProjection>> {}
