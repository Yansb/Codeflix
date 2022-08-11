package com.yansb.admin.api.application.genre.retrieve.get;

import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;

public class DefaultGetGenreByIdUseCase extends GetGenreUseCase {
  private final GenreGateway genreGateway;

  public DefaultGetGenreByIdUseCase(GenreGateway genreGateway) {
    this.genreGateway = genreGateway;
  }

  @Override
  public GenreOutput execute(String anInput) {
    final var aGenreID = GenreID.from(anInput);
    return this.genreGateway.findByID(GenreID.from(anInput))
        .map(GenreOutput::from)
        .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreID));
  }
}
