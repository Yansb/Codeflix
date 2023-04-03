package com.yansb.admin.api.application.castMember.retrieve.list;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt
) {

  public static CastMemberListOutput from(final CastMember aMember) {
    return new CastMemberListOutput(
        aMember.getId().getValue(),
        aMember.getName(),
        aMember.getType(),
        aMember.getCreatedAt()
    );
  }
}

