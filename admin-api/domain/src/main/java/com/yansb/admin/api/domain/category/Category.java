package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.AggregateRoot;
import com.yansb.admin.api.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {
  private String name;
  private String description;
  private boolean active;
  final private Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;

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
    this.createdAt = Objects.requireNonNull(aCreationDate, "'created_at' should not be null");
    this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updated_at' should not be null");
    this.deletedAt = aDeleteDate;
  }

  public static Category newCategory(final String aName, final String aDescription, final Boolean IsActive){
    final var id = CategoryID.unique();
    final var now = Instant.now();
    final var deletedAt = IsActive ? null : Instant.now();
    return new Category(id, aName, aDescription, IsActive, now, now, deletedAt);
  }

  public static Category clone(final Category aCategory){
    return aCategory.clone();
  }

  public static Category with(
      final CategoryID anId,
      final String name,
      final String description,
      final boolean active,
      final Instant createdAt,
      final Instant updatedAt,
      final Instant deletedAt
  ) {
    return new Category(
        anId,
        name,
        description,
        active,
        createdAt,
        updatedAt,
        deletedAt
    );
  }

  @Override
  public void validate(ValidationHandler handler) {
    new CategoryValidator(this, handler).validate();
  }

  public Category activate(){
    this.deletedAt = null;
    this.active = true;
    this.updatedAt = Instant.now();
    return this;
  }

  public Category deactivate(){
    if(getDeletedAt() == null){
      this.deletedAt = Instant.now();
    }

    this.active = false;
    this.updatedAt = Instant.now();
    return this;
  }

  public Category update(final String aName, final String aDescription, final boolean isActive){
    if(isActive){
      activate();
    }else {
      deactivate();
    }
    this.name = aName;
    this.description = aDescription;
    this.updatedAt = Instant.now();
    return this;
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

  @Override
  public Category clone() {
    try {
      return (Category) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }
}