package com.yansb.admin.api.infrastructure.category;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import com.yansb.admin.api.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.yansb.admin.api.infrastructure.utils.SpecificationUtils.like;

@Component
public class CategoryMySQLGateway implements CategoryGateway {
  private CategoryRepository repository;

  public CategoryMySQLGateway(CategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public Category create(final Category aCategory) {
    return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
  }

  @Override
  public void deleteById(CategoryID anID) {
    String anIdValue = anID.getValue();
    if(this.repository.existsById(anIdValue)){
      this.repository.deleteById(anIdValue);
    }
  }

  @Override
  public Optional<Category> findById(final CategoryID anID) {
    return this.repository.findById(anID.getValue())
        .map(CategoryJpaEntity::toAggregate);
  }

  @Override
  public Category update(final Category aCategory) {
    return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
  }

  @Override
  public Pagination<Category> findAll(final SearchQuery aQuery) {
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    final var specifications = Optional.ofNullable(aQuery.terms())
        .filter(str -> !str.isBlank())
        .map(this::assembleSpecification)
        .orElse(null);
    final var pageResult = this.repository.findAll(Specification.where(specifications), page);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(CategoryJpaEntity::toAggregate).toList()
    );
  }

  @Override
  public List<CategoryID> existsByIds(Iterable<CategoryID> ids) {
    //TODO: Implementar quando chegar na parte de infraestrutura
    return Collections.emptyList();
  }

  private Specification<CategoryJpaEntity> assembleSpecification(final String str) {
    final Specification<CategoryJpaEntity> nameLike = like("name", str);
    final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
    return nameLike.or(descriptionLike);
  }
}
