package com.uit.se356.common.mappers;

/**
 * Lớp tiện ích cho việc ánh xạ từ Entity sang Response
 *
 * @param E Entity type, thường là lớp thực thể (Entity)
 * @param O Output type, thường là Response DTO
 * @param S Summary type, thường là dạng rút gọn của O (Summary DTO)
 * @version 1.0
 */
public interface OutputMapper<E, O, S> {
  S entityToResponseSummary(E entity);

  O entityToResponse(E entity);
}
