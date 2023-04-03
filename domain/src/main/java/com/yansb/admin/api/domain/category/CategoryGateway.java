package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);

    void deleteById(CategoryID anID);

    Optional<Category> findById(CategoryID anID);

    Category update(Category aCategory);

    Pagination<Category> findAll(SearchQuery aQuery);

    List<CategoryID> existsByIds(Iterable<CategoryID> ids);


}
