package com.yansb.admin.api.application.category.create;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;

public record CreateCategoryOutput(
    CategoryID id
) {
  public static CreateCategoryOutput from(final Category aCategory){
    return new CreateCategoryOutput(aCategory.getId());
  }
}
