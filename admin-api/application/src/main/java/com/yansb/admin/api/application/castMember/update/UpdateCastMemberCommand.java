package com.yansb.admin.api.application.castMember.update;

import com.yansb.admin.api.domain.castMember.CastMemberType;

public record UpdateCastMemberCommand(
    String id,
    String name,
    CastMemberType type
) {

  public static UpdateCastMemberCommand with(
      final String anId,
      final String aName,
      final CastMemberType aType
  ) {
    return new UpdateCastMemberCommand(anId, aName, aType);
  }
}
