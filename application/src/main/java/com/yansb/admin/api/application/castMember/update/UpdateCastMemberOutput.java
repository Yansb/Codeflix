package com.yansb.admin.api.application.castMember.update;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberID;

public record UpdateCastMemberOutput(String id) {
  public static UpdateCastMemberOutput from(final CastMember aMember) {
    return new UpdateCastMemberOutput(aMember.getId().getValue());
  }

  public static UpdateCastMemberOutput from(final CastMemberID anId) {
    return new UpdateCastMemberOutput(anId.getValue());
  }
}
