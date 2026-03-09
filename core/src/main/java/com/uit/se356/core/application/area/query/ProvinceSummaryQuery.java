package com.uit.se356.core.application.area.query;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.Query;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.application.area.projections.ProvinceSummaryProjection;

public record ProvinceSummaryQuery(SearchPageable pageable)
    implements Query<PageResponse<ProvinceSummaryProjection>> {}
