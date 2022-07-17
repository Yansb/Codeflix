package com.yansb.admin.api.application.category.retrieve.list;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(
    CategoryID id,
    String name,
    String description,
    boolean isActive,
    Instant createdAt,
    Instant deletedAt
){
  public static CategoryListOutput from(final Category aCategory){
    return new CategoryListOutput(
        aCategory.getId(),
        aCategory.getName(),
        aCategory.getDescription(),
        aCategory.getIsActive(),
        aCategory.getCreatedAt(),
        aCategory.getDeletedAt()
    );
  }
}

