package com.yansb.admin.api.infrastructure.castMember.models;

import com.yansb.admin.api.domain.castMember.CastMemberType;

public record UpdateCastMemberRequest(
    String name,
    CastMemberType type
) {
}
