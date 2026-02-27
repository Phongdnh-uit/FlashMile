package com.uit.se356.common.dto;

import java.util.List;

public record SearchPageable(
    String
        filter // Sử dụng biểu thức RSQL để lọc dữ liệu nâng cao, cần có kiểm tra các field nhạy cảm
        ,
    List<String>
        sorts, // Truyền danh sách các trường cần sắp xếp, truyền theo dạng "field,asc" hoặc
    // "field,desc"
    Integer page,
    Integer size) {
  public SearchPageable {
    filter = filter == null ? "" : filter;
    sorts = (sorts == null || sorts.isEmpty()) ? List.of("id,asc") : sorts;
    page = (page == null || page < 0) ? 0 : page;
    int defaultSize = (size == null || size <= 0) ? 10 : size;
    // Ngưỡng trên cho size tránh việc client lạm dụng
    size = Math.min(defaultSize, 100); // Ngưỡng trên tối đa 100
  }

  public static SearchPageable defaultSearch() {
    return new SearchPageable("", null, 0, 10);
  }

  public static SearchPageable of(String filter, List<String> sorts, int page, int size) {
    return new SearchPageable(filter, sorts, page, size);
  }
}
