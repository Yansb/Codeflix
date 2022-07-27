package com.yansb.admin.api.infrastructure.api.controllers;

import com.yansb.admin.api.application.category.create.CreateCategoryCommand;
import com.yansb.admin.api.application.category.create.CreateCategoryOutput;
import com.yansb.admin.api.application.category.create.CreateCategoryUseCase;
import com.yansb.admin.api.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.validation.handler.Notification;
import com.yansb.admin.api.infrastructure.api.CategoryAPI;
import com.yansb.admin.api.infrastructure.category.models.CategoryApiOutput;
import com.yansb.admin.api.infrastructure.category.models.CreateCategoryApiInput;
import com.yansb.admin.api.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {


  private final CreateCategoryUseCase createCategoryUseCase;
  private final GetCategoryByIdUseCase getCategoryByIdUseCase;


  public CategoryController(CreateCategoryUseCase createCategoryUseCase, final GetCategoryByIdUseCase getCategoryByIdUseCase) {
    this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
  }

  @Override
  public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
    final var aCommand = CreateCategoryCommand.with(
        input.name(),
        input.description(),
        input.active() != null ? input.active() : false
    );

    final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

    final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
        ResponseEntity.created(URI.create("/categories/" +output.id())).body(output);

    return this.createCategoryUseCase.execute(aCommand)
        .fold(onError, onSuccess);
  }

  @Override
  public Pagination<?> listCategories(String search, int page, int perPage, int sort, int dir) {
    return null;
  }

  @Override
  public CategoryApiOutput getById(final String id) {
    return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
  }
}
