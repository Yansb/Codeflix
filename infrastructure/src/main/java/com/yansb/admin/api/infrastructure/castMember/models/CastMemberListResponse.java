package com.yansb.admin.api.infrastructure.castMember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yansb.admin.api.domain.castMember.CastMemberType;

public record CastMemberListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type,
    @JsonProperty("created_at") String createdAt
) {
}
