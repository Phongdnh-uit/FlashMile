package com.uit.se356.common.mappers;

import org.mapstruct.MappingTarget;

/**
 * Lớp tiện ích cho việc ánh xạ dữ liệu từ đối tượng cập nhật (Update DTO) sang thực thể (Entity)
 *
 * @param E Entity type, thường là lớp thực thể (Entity)
 * @param IUpdate Input type, thường là DTO dùng để cập nhật (Update DTO)
 * @version 1.0
 */
public interface UpdateMapper<E, IUpdate> {
  void updateToEntity(IUpdate update, @MappingTarget E entity);
}
