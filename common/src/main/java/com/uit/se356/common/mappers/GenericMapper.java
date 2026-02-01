package com.uit.se356.common.mappers;

import org.mapstruct.MappingTarget;

/**
 * Lớp tiện ích cho việc ánh xạ giữa các lớp đối tượng khác nhau
 *
 * @param E Entity type, thường là lớp thực thể (Entity)
 * @param I Input type, thường là Request DTO
 * @param O Output type, thường là Response DTO
 * @param S Summary type, thường là dạng rút gọn của O (Summary DTO)
 * @version 1.0
 */
public interface GenericMapper<E, I, O, S> {
  S entityToResponseSummary(E entity);

  E requestToEntity(I request);

  O entityToResponse(E entity);

  void partialUpdate(I request, @MappingTarget E entity);
}
