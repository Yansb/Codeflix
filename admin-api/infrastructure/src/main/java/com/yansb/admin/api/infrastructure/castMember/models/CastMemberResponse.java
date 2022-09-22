package com.yansb.admin.api.infrastructure.castMember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yansb.admin.api.domain.castMember.CastMemberType;

import java.time.Instant;

public record CastMemberResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type,
    @JsonProperty("created_at") Instant created_at,
    @JsonProperty("updated_at") Instant updated_at
) {
}
