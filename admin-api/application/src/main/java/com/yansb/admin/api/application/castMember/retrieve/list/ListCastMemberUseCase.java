package com.yansb.admin.api.application.castMember.retrieve.list;

import com.yansb.admin.api.application.UseCase;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;

public sealed abstract class ListCastMemberUseCase
    extends UseCase<SearchQuery, Pagination<CastMemberListOutput>> permits DefaultListCastMemberUseCase {
}
