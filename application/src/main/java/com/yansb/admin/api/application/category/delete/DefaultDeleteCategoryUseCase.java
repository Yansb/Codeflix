package com.yansb.admin.api.application.category.delete;

import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{
  private final CategoryGateway categoryGateway;

  public DefaultDeleteCategoryUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }


  @Override
  public void execute(String anInput) {
    categoryGateway.deleteById(CategoryID.from(anInput));
  }
}
