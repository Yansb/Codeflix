package com.yansb.admin.api.application.castMember.create;

import com.yansb.admin.api.domain.castMember.CastMemberType;

public record CreateCastMemberCommand(
    String name,
    CastMemberType type
) {
  public static CreateCastMemberCommand with(final String aName, final CastMemberType aType) {
    return new CreateCastMemberCommand(aName, aType);
  }
}
