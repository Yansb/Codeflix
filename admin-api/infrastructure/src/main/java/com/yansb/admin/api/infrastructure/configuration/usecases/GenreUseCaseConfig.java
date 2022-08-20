package com.yansb.admin.api.infrastructure.configuration.usecases;

import com.yansb.admin.api.application.genre.create.CreateGenreUseCase;
import com.yansb.admin.api.application.genre.create.DefaultCreateGenreUseCase;
import com.yansb.admin.api.application.genre.delete.DefaultDeleteGenreUseCase;
import com.yansb.admin.api.application.genre.delete.DeleteGenreUseCase;
import com.yansb.admin.api.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.yansb.admin.api.application.genre.retrieve.get.GetGenreUseCase;
import com.yansb.admin.api.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.yansb.admin.api.application.genre.retrieve.list.ListGenreUseCase;
import com.yansb.admin.api.application.genre.update.DefaultUpdateGenreUseCase;
import com.yansb.admin.api.application.genre.update.UpdateGenreUseCase;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;

import java.util.Objects;

public class GenreUseCaseConfig {

  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;

  public GenreUseCaseConfig(final GenreGateway genreGateway, final CategoryGateway categoryGateway) {
    this.genreGateway = Objects.requireNonNull(genreGateway);
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }

  @Bean
  public CreateGenreUseCase createGenreUseCase() {
    return new DefaultCreateGenreUseCase(categoryGateway, genreGateway);
  }

  @Bean
  public DeleteGenreUseCase deleteGenreUseCase() {
    return new DefaultDeleteGenreUseCase(genreGateway);
  }

  @Bean
  public GetGenreUseCase getGenreUseCase() {
    return new DefaultGetGenreByIdUseCase(genreGateway);
  }

  @Bean
  public ListGenreUseCase listGenreUseCase() {
    return new DefaultListGenreUseCase(genreGateway);
  }

  @Bean
  public UpdateGenreUseCase updateGenreUseCase() {
    return new DefaultUpdateGenreUseCase(categoryGateway,genreGateway);
  }


}
