package com.yansb.admin.api.application.castMember.update;

import com.yansb.admin.api.domain.castMember.CastMember;

public record UpdateCastMemberOutput(String id) {
  public static UpdateCastMemberOutput from(final CastMember aMember) {
    return new UpdateCastMemberOutput(aMember.getId().getValue());
  }
}
