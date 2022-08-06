package com.yansb.admin.api.domain.genre;

import com.yansb.admin.api.domain.AggregateRoot;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.utils.InstantUtils;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {

  private String name;
  private boolean active;
  private List<CategoryID> categories;
  private final Instant createdAt;
  private Instant updatedAt;
  private Instant deletedAt;


  protected Genre(
      final GenreID anId,
      final String aName,
      final boolean isActive,
      final List<CategoryID> categories,
      final Instant aCreatedAt,
      final Instant aUpdatedAt,
      final Instant aDeletedAt
      ){
    super(anId);
    this.name = aName;
    this.active = isActive;
    this.categories = categories;
    this.createdAt = aCreatedAt;
    this.updatedAt = aUpdatedAt;
    this.deletedAt = aDeletedAt;

    selfValidate();
  }

  public static Genre newGenre(final String aName, final boolean isActive){
    final var anId = GenreID.unique();
    final var now = InstantUtils.now();
    final var deletedAt = isActive ? null : now;
    return new Genre(
        anId,
        aName,
        isActive,
        new ArrayList<>(),
        now,
        now,
        deletedAt
    );
  }

  public static Genre with(
      final GenreID anId,
      final String aName,
      final boolean isActive,
      final List<CategoryID> categories,
      final Instant aCreatedAt,
      final Instant aUpdatedAt,
      final Instant aDeletedAt
  ){
    return new Genre(
        anId,
        aName,
        isActive,
        categories,
        aCreatedAt,
        aUpdatedAt,
        aDeletedAt
    );
  }


  public static Genre with(final Genre aGenre){
    return new Genre(
        aGenre.id,
        aGenre.name,
        aGenre.isActive(),
        new ArrayList<>(aGenre.categories),
        aGenre.createdAt,
        aGenre.updatedAt,
        aGenre.deletedAt
    );
  }


  @Override
  public void validate(ValidationHandler handler) {
    new GenreValidator(this, handler).validate();
  }

  public Genre activate(){
    this.deletedAt = null;
    this.active = true;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Genre deactivate(){
    if(getDeletedAt() == null){
      this.deletedAt = InstantUtils.now();
    }
    this.active = false;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories) {
    if(isActive){
      activate();
    }else{
      deactivate();
    }
    this.name = aName;
    this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
    this.updatedAt = InstantUtils.now();
    selfValidate();
    return this;
  }

  public void addCategory(final CategoryID aCategoryId){
    if(aCategoryId == null){
      return;
    }

    this.categories.add(aCategoryId);
    this.updatedAt = InstantUtils.now();
  }

  public void removeCategory(final CategoryID aCategoryId){
    if(aCategoryId == null){
      return;
    }

    this.categories.remove(aCategoryId);
    this.updatedAt = InstantUtils.now();
  }

  public String getName() {
    return name;
  }

  public List<CategoryID> getCategories() {
    return Collections.unmodifiableList(categories);
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

  public boolean isActive() {
    return active;
  }

  private void selfValidate() {
    final var notification = Notification.create();
    validate(notification);

    if(notification.hasError()){
      throw new NotificationException("Failed to create a Aggregate Genre",notification);
    }
  }


}
