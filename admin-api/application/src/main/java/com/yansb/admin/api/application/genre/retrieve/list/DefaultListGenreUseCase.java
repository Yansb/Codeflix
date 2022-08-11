package com.yansb.admin.api.application.genre.retrieve.list;

import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenreUseCase extends ListGenreUseCase {

  private final GenreGateway genreGateway;

  public DefaultListGenreUseCase(final GenreGateway genreGateway) {
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }


  @Override
  public Pagination<GenreListOutput> execute(SearchQuery aQuery) {
    return this.genreGateway.findAll(aQuery)
        .map(GenreListOutput::from);
  }
}
