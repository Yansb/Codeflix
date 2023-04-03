package com.yansb.admin.api.domain.castMember;

import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
  CastMember create(CastMember aCastMember);

  void deleteByID(CastMemberID anID);

  Optional<CastMember> findByID(CastMemberID anID);

  CastMember update(CastMember aCastMember);

  Pagination<CastMember> findAll(SearchQuery aQuery);

  List<CastMemberID> existsByIds(Iterable<CastMemberID> ids);
}
