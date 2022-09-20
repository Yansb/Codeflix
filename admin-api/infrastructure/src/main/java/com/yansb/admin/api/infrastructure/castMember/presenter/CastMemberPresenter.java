package com.yansb.admin.api.infrastructure.castMember.presenter;

import com.yansb.admin.api.application.castMember.retrieve.get.CastMemberOutput;
import com.yansb.admin.api.application.castMember.retrieve.list.CastMemberListOutput;
import com.yansb.admin.api.infrastructure.castMember.models.CastMemberListResponse;
import com.yansb.admin.api.infrastructure.castMember.models.CastMemberResponse;

public interface CastMemberPresenter {
  static CastMemberResponse present(final CastMemberOutput aMember) {
    return new CastMemberResponse(
        aMember.id(),
        aMember.name(),
        aMember.type(),
        aMember.createdAt(),
        aMember.updatedAt()
    );
  }

  static CastMemberListResponse present(final CastMemberListOutput aMember) {
    return new CastMemberListResponse(
        aMember.id(),
        aMember.name(),
        aMember.type(),
        aMember.createdAt().toString()
    );
  }
}
