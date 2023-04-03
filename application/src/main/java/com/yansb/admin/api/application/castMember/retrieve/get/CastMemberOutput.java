package com.yansb.admin.api.application.castMember.retrieve.get;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt,
    Instant updatedAt
) {

  public static CastMemberOutput from(final CastMember aMember) {
    return new CastMemberOutput(
        aMember.getId().getValue(),
        aMember.getName(),
        aMember.getType(),
        aMember.getCreatedAt(),
        aMember.getUpdatedAt()
    );
  }
}
