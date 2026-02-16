package com.uit.se356.common.services;

import com.uit.se356.common.dto.Query;

/** Bus cho các query */
public interface QueryBus {
  <R> R dispatch(Query<R> query);
}
