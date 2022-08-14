package com.yansb.admin.api.infrastructure.genre;

import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {
  @Override
  public Genre create(Genre aGenre) {
    return null;
  }

  @Override
  public void deleteByID(GenreID anID) {

  }

  @Override
  public Optional<Genre> findByID(GenreID anID) {
    return Optional.empty();
  }

  @Override
  public Genre update(Genre aGenre) {
    return null;
  }

  @Override
  public Pagination<Genre> findAll(SearchQuery aQuery) {
    return null;
  }
}
