package com.yansb.admin.api.application.category.create;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.validation.handler.Notification;
import com.yansb.admin.api.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.*;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {
  private final CategoryGateway categoryGateway;

  public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }


  @Override
  public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand anInput) {
    final var aName = anInput.name();
    final var aDescription = anInput.description();
    final var isActive = anInput.isActive();

    final var notification = Notification.create();
    final var aCategory = Category.newCategory(aName, aDescription, isActive);
    aCategory.validate(notification);

    return notification.hasError() ? Left(notification) : create(aCategory);
  }

  private Either<Notification, CreateCategoryOutput> create(Category aCategory) {
    return Try(() -> this.categoryGateway.create(aCategory))
        .toEither()
        .bimap(Notification::create, CreateCategoryOutput::from);
  }
}
