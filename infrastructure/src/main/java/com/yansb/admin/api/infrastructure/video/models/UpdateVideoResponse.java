package com.yansb.admin.api.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoResponse(
        @JsonProperty("id") String id
) {
}
