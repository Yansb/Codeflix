package com.yansb.admin.api.infrastructure.api.controllers;

import com.yansb.admin.api.application.category.create.CreateCategoryCommand;
import com.yansb.admin.api.application.category.create.CreateCategoryOutput;
import com.yansb.admin.api.application.category.create.CreateCategoryUseCase;
import com.yansb.admin.api.application.category.delete.DeleteCategoryUseCase;
import com.yansb.admin.api.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.yansb.admin.api.application.category.retrieve.list.ListCategoriesUseCase;
import com.yansb.admin.api.application.category.update.UpdateCategoryCommand;
import com.yansb.admin.api.application.category.update.UpdateCategoryOutput;
import com.yansb.admin.api.application.category.update.UpdateCategoryUseCase;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.validation.handler.Notification;
import com.yansb.admin.api.infrastructure.api.CategoryAPI;
import com.yansb.admin.api.infrastructure.category.models.CategoryListResponse;
import com.yansb.admin.api.infrastructure.category.models.CategoryResponse;
import com.yansb.admin.api.infrastructure.category.models.CreateCategoryRequest;
import com.yansb.admin.api.infrastructure.category.models.UpdateCategoryRequest;
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
  private final UpdateCategoryUseCase updateCategoryUseCase;
  private final DeleteCategoryUseCase deleteCategoryUseCase;
  private final ListCategoriesUseCase listCategoriesUseCase;

  public CategoryController(CreateCategoryUseCase createCategoryUseCase, final GetCategoryByIdUseCase getCategoryByIdUseCase, final UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
    this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
    this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
  }

  @Override
  public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
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
  public Pagination<CategoryListResponse> listCategories(
          final String search,
          final int page,
          final int perPage,
          final String sort,
          final String dir
  ) {
    return listCategoriesUseCase.execute(
        new SearchQuery(page, perPage, search, sort, dir)
    )
        .map(CategoryApiPresenter::present);
  }

  @Override
  public CategoryResponse getById(final String id) {
    return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
  }

  @Override
  public ResponseEntity<?> updateById(String id, UpdateCategoryRequest input) {
    final var aCommand = UpdateCategoryCommand.with(
        id,
        input.name(),
        input.description(),
        input.active() != null ? input.active() : false
    );

    final Function<Notification, ResponseEntity<?>> onError =
        ResponseEntity.unprocessableEntity()::body;

    final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
        ResponseEntity::ok;

    return this.updateCategoryUseCase.execute(aCommand)
        .fold(onError, onSuccess);
  }

  @Override
  public void deleteById(final String id) {
    this.deleteCategoryUseCase.execute(id);
  }
}
