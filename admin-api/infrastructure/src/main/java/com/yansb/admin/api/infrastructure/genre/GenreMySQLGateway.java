package com.yansb.admin.api.infrastructure.genre;

import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreJpaEntity;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreMySQLGateway implements GenreGateway {

  private final GenreRepository genreRepository;

  public GenreMySQLGateway(final GenreRepository genreRepository) {
    this.genreRepository = Objects.requireNonNull(genreRepository);
  }

  @Override
  public Genre create(Genre aGenre) {
    return save(aGenre);
  }

  private Genre save(Genre aGenre) {
    return this.genreRepository.save(GenreJpaEntity.from(aGenre))
        .toAggregate();
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
    return save(aGenre);
  }

  @Override
  public Pagination<Genre> findAll(SearchQuery aQuery) {
    return null;
  }
}
