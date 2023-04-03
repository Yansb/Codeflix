package com.yansb.admin.api.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yansb.admin.api.domain.video.VideoMediaType;

public record UploadMediaResponse(
        @JsonProperty("video_id") String videoId,
        @JsonProperty("media_type") VideoMediaType mediaType
) {
}
