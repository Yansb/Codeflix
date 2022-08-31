package com.yansb.admin.api.application.castMember.create;

import com.yansb.admin.api.domain.castMember.CastMember;

public record CreateCastMemberOutput(
        String id
) {

    public static CreateCastMemberOutput from(final CastMember aMember){
        return  new CreateCastMemberOutput(aMember.getId().getValue());
    }
}
