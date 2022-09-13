package com.yansb.admin.api.infrastructure.castMember.models;

import com.yansb.admin.api.domain.castMember.CastMemberType;

import java.time.Instant;

public record CastMemberResponse(
    String id,
    String name,
    CastMemberType type,
    Instant created_at,
    Instant updated_at
) {
}
