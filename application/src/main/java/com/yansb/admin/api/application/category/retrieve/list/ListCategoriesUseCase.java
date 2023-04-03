package com.yansb.admin.api.application.category.retrieve.list;

import com.yansb.admin.api.application.UseCase;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;

public abstract class ListCategoriesUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
