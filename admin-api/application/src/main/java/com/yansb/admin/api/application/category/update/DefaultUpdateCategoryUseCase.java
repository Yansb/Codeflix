package com.yansb.admin.api.application.category.update;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {
  private final CategoryGateway categoryGateway;

  public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }


  @Override
  public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand anInput) {
    final var anId = CategoryID.from(anInput.id());
    final var aName = anInput.name();
    final var aDescription = anInput.description();
    final var isActive = anInput.isActive();

    final var aCategory = this.categoryGateway.findById(anId)
        .orElseThrow(notFound(anId));

    final var notification = Notification.create();
    aCategory
        .update(aName, aDescription, isActive)
        .validate(notification);

    return notification.hasError() ? Left(notification) : update(aCategory);
  }

  private Either<Notification, UpdateCategoryOutput> update(final Category aCategory){
    return API.Try(() -> this.categoryGateway.update(aCategory))
            .toEither()
            .bimap(Notification::create, UpdateCategoryOutput::from);
  }

  public Supplier<NotFoundException> notFound(final CategoryID anId) {
    return () -> NotFoundException.with(Category.class, anId);
  }
}
