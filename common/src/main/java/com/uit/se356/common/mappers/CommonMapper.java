package com.uit.se356.common.mappers;

/**
 * Lớp tiện ích cho việc ánh xạ giữa các lớp đối tượng khác nhau
 *
 * @param E Entity type, thường là lớp thực thể (Entity)
 * @param ICreate Input type cho thao tác tạo mới, thường là Request DTO
 * @param IUpdate Input type cho thao tác cập nhật, thường là Request DTO
 * @param O Output type, thường là Response DTO
 * @param S Summary type, thường là dạng rút gọn của O (Summary DTO)
 * @version 2.0
 */
public interface CommonMapper<E, ICreate, IUpdate, O, S>
    extends CreateMapper<E, ICreate>, UpdateMapper<E, IUpdate>, OutputMapper<E, O, S> {}
