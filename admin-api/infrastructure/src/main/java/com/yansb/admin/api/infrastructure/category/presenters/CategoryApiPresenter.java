package com.yansb.admin.api.infrastructure.category.presenters;

import com.yansb.admin.api.application.category.retrieve.get.CategoryOutput;
import com.yansb.admin.api.infrastructure.category.models.CategoryApiOutput;

public interface CategoryApiPresenter {
  static CategoryApiOutput present(final CategoryOutput output){
    return new CategoryApiOutput(
        output.id().getValue(),
        output.name(),
        output.description(),
        output.isActive(),
        output.createdAt(),
        output.updatedAt(),
        output.deletedAt()
    );
  }
}
