package com.yansb.admin.api.infrastructure.castMember.models;

import com.yansb.admin.api.domain.castMember.CastMemberType;

public record CastMemberListResponse(
    String id,
    String name,
    CastMemberType type,
    String createdAt
) {
}
