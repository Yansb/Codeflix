package com.yansb.admin.api.application.castMember.create;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberID;

public record CreateCastMemberOutput(
    String id
) {

    public static CreateCastMemberOutput from(final CastMember aMember) {
        return new CreateCastMemberOutput(aMember.getId().getValue());
    }

    public static CreateCastMemberOutput from(final CastMemberID anId) {
        return new CreateCastMemberOutput(anId.getValue());
    }
}
