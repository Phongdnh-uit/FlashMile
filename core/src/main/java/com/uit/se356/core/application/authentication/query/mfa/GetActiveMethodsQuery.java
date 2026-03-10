package com.uit.se356.core.application.authentication.query.mfa;

import com.uit.se356.common.dto.Query;
import com.uit.se356.core.application.authentication.projections.MfaMethodProjection;
import java.util.List;

public record GetActiveMethodsQuery() implements Query<List<MfaMethodProjection>> {}
