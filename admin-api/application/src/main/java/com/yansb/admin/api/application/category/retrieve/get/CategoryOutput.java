package com.yansb.admin.api.application.category.retrieve.get;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(
    CategoryID id,
    String name,
    String description,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
    ){
  public static CategoryOutput from(final Category aCategory){
    return new CategoryOutput(
        aCategory.getId(),
        aCategory.getName(),
        aCategory.getDescription(),
        aCategory.getIsActive(),
        aCategory.getCreatedAt(),
        aCategory.getUpdatedAt(),
        aCategory.getDeletedAt()
    );
  }
}
