package com.yansb.admin.api.domain.genre;

import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {
  Genre create(Genre aGenre);
  void deleteByID(GenreID anID);
  Optional<Genre> findByID(GenreID anID);
  Genre update(Genre aGenre);
  Pagination<Genre> findAll(SearchQuery aQuery);

}
