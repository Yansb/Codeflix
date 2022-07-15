package com.yansb.admin.api.application;

import com.yansb.admin.api.domain.category.Category;

public class UseCase {
  public Category execute() {
    return Category.newCategory("teste", "description", false);
  }
}