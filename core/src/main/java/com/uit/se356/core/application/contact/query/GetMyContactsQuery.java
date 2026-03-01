package com.uit.se356.core.application.contact.query;

import com.uit.se356.common.dto.Query;
import com.uit.se356.core.application.contact.result.ContactResult;
import com.uit.se356.core.domain.vo.authentication.UserId;
import java.util.List;

public record GetMyContactsQuery(UserId userId) implements Query<List<ContactResult>> {}
