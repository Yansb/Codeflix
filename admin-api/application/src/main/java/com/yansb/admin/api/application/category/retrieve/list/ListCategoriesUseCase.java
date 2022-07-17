package com.yansb.admin.api.application.category.retrieve.list;

import com.yansb.admin.api.application.UseCase;
import com.yansb.admin.api.domain.category.CategorySearchQuery;
import com.yansb.admin.api.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
