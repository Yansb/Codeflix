package com.yansb.admin.api.application.genre.delete;

import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;

import java.util.Objects;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {
  private final GenreGateway genreGateway;

  public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }

  @Override
  public void execute(final String anIn) {
    this.genreGateway.deleteByID(GenreID.from(anIn));
  }
}
