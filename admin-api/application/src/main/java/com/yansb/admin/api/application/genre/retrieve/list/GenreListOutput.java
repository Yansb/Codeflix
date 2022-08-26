package com.yansb.admin.api.application.genre.retrieve.list;

import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreID;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
    GenreID id,
    String name,
    boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant deletedAt
) {

  public static GenreListOutput from(final Genre genre) {
    return new GenreListOutput(
        genre.getId(),
        genre.getName(),
        genre.isActive(),
        genre.getCategories().stream().map(CategoryID::getValue).toList(),
        genre.getCreatedAt(),
        genre.getDeletedAt()
    );
  }
}
