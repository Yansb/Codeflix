package com.yansb.admin.api.infrastructure.configuration.usecases;

import com.yansb.admin.api.application.category.create.CreateCategoryUseCase;
import com.yansb.admin.api.application.category.create.DefaultCreateCategoryUseCase;
import com.yansb.admin.api.application.category.delete.DefaultDeleteCategoryUseCase;
import com.yansb.admin.api.application.category.delete.DeleteCategoryUseCase;
import com.yansb.admin.api.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.yansb.admin.api.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.yansb.admin.api.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.yansb.admin.api.application.category.retrieve.list.ListCategoriesUseCase;
import com.yansb.admin.api.application.category.update.DefaultUpdateCategoryUseCase;
import com.yansb.admin.api.application.category.update.UpdateCategoryUseCase;
import com.yansb.admin.api.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

  private final CategoryGateway categoryGateway;

  public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Bean
  public CreateCategoryUseCase createCategoryUseCase(){
    return new DefaultCreateCategoryUseCase(categoryGateway);
  }

  @Bean
  public UpdateCategoryUseCase updateCategoryUseCase(){
    return new DefaultUpdateCategoryUseCase(categoryGateway);
  }

  @Bean
  public GetCategoryByIdUseCase getCategoryByIdUseCase(){
    return new DefaultGetCategoryByIdUseCase(categoryGateway);
  }

  @Bean
  public ListCategoriesUseCase listCategoriesUseCase(){
    return new DefaultListCategoriesUseCase(categoryGateway);
  }

  @Bean
  public DeleteCategoryUseCase deleteCategoryUseCase(){
    return new DefaultDeleteCategoryUseCase(categoryGateway);
  }
}
