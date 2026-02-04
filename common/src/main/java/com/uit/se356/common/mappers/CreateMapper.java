package com.uit.se356.common.mappers;

/**
 * Lớp tiện ích cho việc ánh xạ từ đối tượng tạo (Create) sang thực thể (Entity)
 *
 * @param E Entity type, thường là lớp thực thể (Entity)
 * @param ICreate Input type cho việc tạo mới, thường là Request DTO dành cho tạo mới
 * @version 1.0
 */
public interface CreateMapper<E, ICreate> {
  E createToEntity(ICreate create);
}
