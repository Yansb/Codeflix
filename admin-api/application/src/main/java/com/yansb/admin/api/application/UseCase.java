package com.yansb.admin.api.application;

import com.yansb.admin.api.domain.category.Category;

public abstract class UseCase<IN, OUT> {
  public abstract OUT execute(IN anInput);
}