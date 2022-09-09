package com.yansb.admin.api.infrastructure.castMember;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberJpaEntity;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

  private final CastMemberRepository castMemberRepository;

  public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
    this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
  }

  @Override
  public CastMember create(final CastMember aCastMember) {
    return save(aCastMember);
  }

  @Override
  public void deleteByID(final CastMemberID anID) {

  }

  @Override
  public Optional<CastMember> findByID(final CastMemberID anID) {
    return Optional.empty();
  }

  @Override
  public CastMember update(final CastMember aCastMember) {
    return null;
  }

  @Override
  public Pagination<CastMember> findAll(final SearchQuery aQuery) {
    return null;
  }

  private CastMember save(CastMember aCastMember) {
    return this.castMemberRepository.save(CastMemberJpaEntity.from(aCastMember))
        .toAggregate();
  }
}
