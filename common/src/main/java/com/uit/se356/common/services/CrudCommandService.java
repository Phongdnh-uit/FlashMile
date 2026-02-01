package com.uit.se356.common.services;

import com.uit.se356.common.mappers.GenericMapper;
import com.uit.se356.common.repository.CommonRepository;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp tiện ích cho thao tác thay đổi dữ liệu
 *
 * @param I Input đầu vào, thường là Request DTO
 * @param O Output đầu ra, thường là Response DTO
 * @param ID Kiểu dữ liệu của định danh (ID) của thực thể
 * @version 1.0
 */
public interface CrudCommandService<I, O, ID> {
  // ============================ CONTRACT ============================
  O create(I input);

  O update(ID id, I input);

  void delete(ID id);

  void deleteAll(Iterable<ID> ids);

  // ============================ CREATE UTIL ============================
  default void defaultCreateValidate(I input, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác trước khi ánh xạ dữ liệu
    // Ví dụ: Kiểm tra hợp lệ dữ liệu, thiết lập giá trị mặc định, v.v.
  }

  default <E> void defaultCreateEnrich(I input, E entity, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác sau khi ánh xạ dữ liệu
    // Ví dụ: Thiết lập các thuộc tính phức tạp, liên kết với các thực thể khác, v.v.
  }

  default <E> void defaultAfterCreate(E entity, O output, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác sau khi tạo thực thể
    // Ví dụ: Ghi log, gửi thông báo, v.v.
  }

  default <E, S> O defaultCreate(
      I input, CommonRepository<E, ID> repository, GenericMapper<E, I, O, S> mapper) {
    Map<String, Object> context = new HashMap<>();
    defaultCreateValidate(input, context);
    E entity = mapper.requestToEntity(input);
    defaultCreateEnrich(input, entity, context);
    entity = repository.save(entity);
    O output = mapper.entityToResponse(entity);
    defaultAfterCreate(entity, output, context);
    return output;
  }

  // ============================ UPDATE UTIL ============================
  default <E> void defaultUpdateValidate(
      ID id, I input, E existingEntity, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác trước khi ánh xạ dữ liệu
    // Ví dụ: Kiểm tra hợp lệ dữ liệu, xác thực quyền truy cập, v.v.
  }

  default <E> void defaultUpdateEnrich(
      ID id, I input, E existingEntity, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác sau khi ánh xạ dữ liệu
    // Ví dụ: Cập nhật các thuộc tính phức tạp, liên kết với các thực thể khác, v.v.
  }

  default <E> void defaultAfterUpdate(
      ID id, I input, E updatedEntity, O output, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác sau khi cập nhật thực thể
    // Ví dụ: Ghi log, gửi thông báo, v.v.
  }

  default <E, S> O defaultUpdate(
      ID id, I input, CommonRepository<E, ID> repository, GenericMapper<E, I, O, S> mapper) {
    Map<String, Object> context = new HashMap<>();
    E existingEntity = repository.findById(id).orElseThrow(); // TODO: Trả về ngoại lệ phù hợp
    defaultUpdateValidate(id, input, existingEntity, context);
    mapper.partialUpdate(input, existingEntity);
    defaultUpdateEnrich(id, input, existingEntity, context);
    existingEntity = repository.save(existingEntity);
    O output = mapper.entityToResponse(existingEntity);
    defaultAfterUpdate(id, input, existingEntity, output, context);
    return output;
  }

  // ============================ DELETE UTIL ============================
  default <E> void defaultDeleteValidate(ID id, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác trước khi xóa thực thể
    // Ví dụ: Kiểm tra ràng buộc dữ liệu, xác thực quyền truy cập, v.v.
  }

  default <E> void defaultAfterDelete(ID id, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác sau khi xóa thực thể
    // Ví dụ: Ghi log, gửi thông báo, v.v.
  }

  default <E> void defaultDelete(ID id, CommonRepository<E, ID> repository) {
    Map<String, Object> context = new HashMap<>();
    defaultDeleteValidate(id, context);
    // TODO: Hàm luôn trả thành công ngay cả khi id không tồn tại, cần xem xét kiểm tra trước
    repository.deleteById(id);
    defaultAfterDelete(id, context);
  }

  // ============================ DELETE ALL UTIL ============================
  default <E> void defaultDeleteAllValidate(Iterable<ID> ids, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác trước khi xóa nhiều thực thể
    // Ví dụ: Kiểm tra ràng buộc dữ liệu, xác thực quyền truy cập, v.v.
  }

  default <E> void defaultAfterDeleteAll(Iterable<ID> ids, Map<String, Object> context) {
    // Hook method để thực hiện các thao tác sau khi xóa nhiều thực thể
    // Ví dụ: Ghi log, gửi thông báo, v.v.
  }

  default <E> void defaultDeleteAll(Iterable<ID> ids, CommonRepository<E, ID> repository) {
    Map<String, Object> context = new HashMap<>();
    defaultDeleteAllValidate(ids, context);
    // TODO: Hàm luôn trả thành công ngay cả khi id không tồn tại, cần xem xét kiểm tra trước
    repository.deleteAllByIdInBatch(ids);
    defaultAfterDeleteAll(ids, context);
  }
}
