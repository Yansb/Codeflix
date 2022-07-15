package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.AggregateRoot;

import java.time.Instant;
import java.util.UUID;

public class Category extends AggregateRoot<CategoryID> {
  final private String name;
  final private String description;
  final private boolean active;
  final private Instant createdAt;
  final private Instant updatedAt;
  final private Instant deletedAt;

  private Category(
      final CategoryID anId,
      final String aName,
      final String aDescription,
      final boolean isActive,
      final Instant aCreationDate,
      final Instant aUpdateDate,
      final Instant aDeleteDate
  ) {
    super(anId);
    this.name = aName;
    this.description = aDescription;
    this.active = isActive;
    this.createdAt = aCreationDate;
    this.updatedAt = aUpdateDate;
    this.deletedAt = aDeleteDate;
  }

  public static Category newCategory(final String aName, final String aDescription, final Boolean aIsActive){
    final var id = CategoryID.unique();
    final var now = Instant.now();
    return new Category(id, aName, aDescription, aIsActive, now, now, null);
  }

  public CategoryID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean getIsActive() {
    return active;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Instant getDeletedAt() {
    return deletedAt;
  }
}