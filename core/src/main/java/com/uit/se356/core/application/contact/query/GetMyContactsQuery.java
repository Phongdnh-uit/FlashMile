package com.uit.se356.core.application.contact.query;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.dto.Query;
import com.uit.se356.common.dto.SearchPageable;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.vo.authentication.UserId;

public record GetMyContactsQuery(UserId userId, SearchPageable pageable)
    implements Query<PageResponse<ContactResult>> {}
