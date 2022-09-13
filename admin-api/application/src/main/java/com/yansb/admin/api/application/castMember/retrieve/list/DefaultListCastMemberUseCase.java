package com.yansb.admin.api.application.castMember.retrieve.list;

import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;

import java.util.Objects;

public non-sealed class DefaultListCastMemberUseCase extends ListCastMemberUseCase {
  private final CastMemberGateway castMemberGateway;

  public DefaultListCastMemberUseCase(final CastMemberGateway castMemberGateway) {
    this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
  }


  @Override
  public Pagination<CastMemberListOutput> execute(final SearchQuery aQuery) {
    return this.castMemberGateway.findAll(aQuery)
        .map(CastMemberListOutput::from);
  }
}
