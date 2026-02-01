package com.uit.se356.common.services;

import com.uit.se356.common.dto.PageResponse;
import com.uit.se356.common.mappers.GenericMapper;
import com.uit.se356.common.repository.CommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Lớp tiện ích cho thao tác thay đổi dữ liệu
 *
 * @param E Kiểu thực thể (Entity)
 * @param ID Kiểu dữ liệu của định danh (ID) của thực thể
 * @param S Output đầu ra của truy vấn, thường là dạng rút gọn của O (Summary DTO)
 * @param O Output đầu ra, thường là Response DTO
 * @version 1.0
 */
public interface CrudQueryService<E, ID, S, O> {
  // ============================ CONTRACT ============================
  PageResponse<S> findAll(Pageable pageable, Specification<E> specification);

  O findById(ID id);

  // ============================ FIND ALL UTIL ============================
  default void defaultBeforeFindAll(Pageable pageable, Specification<E> specification) {
    // Chèn các logic dùng chung ở đây nếu cần
  }

  default void defaultAfterFindAll(PageResponse<S> response) {
    // Chèn các logic dùng chung ở đây nếu cần
  }

  default <I> PageResponse<S> defaultFindAll(
      Pageable pageable,
      Specification<E> specification,
      GenericMapper<E, I, O, S> mapper,
      CommonRepository<E, ID> repository) {
    defaultBeforeFindAll(pageable, specification);
    Page<E> entitiesPage = repository.findAll(specification, pageable);
    PageResponse<S> response = PageResponse.from(entitiesPage, mapper::entityToResponseSummary);
    defaultAfterFindAll(response);
    return response;
  }

  // ============================ FIND BY ID UTIL ============================
  default void defaultBeforeFindById(ID id) {
    // Chèn các logic dùng chung ở đây nếu cần
  }

  default void defaultAfterFindById(O response) {
    // Chèn các logic dùng chung ở đây nếu cần
  }

  default <I> O defaultFindById(
      ID id, GenericMapper<E, I, O, S> mapper, CommonRepository<E, ID> repository) {
    defaultBeforeFindById(id);
    E entity = repository.findById(id).orElseThrow();
    O response = mapper.entityToResponse(entity);
    defaultAfterFindById(response);
    return response;
  }
}
