package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
  Category create(Category aCategory);
  void deleteById(CategoryID anID);
  Optional<Category> findById(CategoryID anID);
  Category update(Category aCategory);
  Pagination<Category> findAll(SearchQuery aQuery);


}
