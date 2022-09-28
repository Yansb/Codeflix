package com.yansb.admin.api.infrastructure.genre;

import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreJpaEntity;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import com.yansb.admin.api.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
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
    final var aGenreId = anID.getValue();
    if (this.genreRepository.existsById(aGenreId)) {
      this.genreRepository.deleteById(aGenreId);
    }

  }

  @Override
  public Optional<Genre> findByID(GenreID anID) {
    final var aGenreId = anID.getValue();
    return this.genreRepository.findById(aGenreId)
        .map(GenreJpaEntity::toAggregate);
  }

  @Override
  public Genre update(Genre aGenre) {
    return save(aGenre);
  }

  @Override
  public Pagination<Genre> findAll(SearchQuery aQuery) {
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    final var where = Optional.ofNullable(aQuery.terms())
        .filter(str -> !str.isBlank())
        .map(this::assembleSpecification)
        .orElse(null);

    final var pageResult =
        this.genreRepository.findAll(Specification.where(where), page);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(GenreJpaEntity::toAggregate).stream().toList()
    );
  }

  @Override
  public List<GenreID> existsByIds(Iterable<GenreID> ids) {
    throw new UnsupportedOperationException();
  }

  private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
    return SpecificationUtils.like("name", terms);
  }
}
