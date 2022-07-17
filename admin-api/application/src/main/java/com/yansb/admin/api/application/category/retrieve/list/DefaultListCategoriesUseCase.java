package com.yansb.admin.api.application.category.retrieve.list;

import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategorySearchQuery;
import com.yansb.admin.api.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

  private final CategoryGateway categoryGateway;

  public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }


  @Override
  public Pagination<CategoryListOutput> execute(final CategorySearchQuery aQuery) {
    return this.categoryGateway.findAll(aQuery)
        .map(CategoryListOutput::from);
  }
}
