package com.yansb.admin.api.application.category.update;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;

public record UpdateCategoryOutput(CategoryID id) {

  public static UpdateCategoryOutput from (final Category aCategory){
    return new UpdateCategoryOutput(aCategory.getId());
  }
}
