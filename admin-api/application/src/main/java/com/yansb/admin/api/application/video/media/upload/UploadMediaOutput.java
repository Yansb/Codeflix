package com.yansb.admin.api.application.video.media.upload;

import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoMediaType;

public record UploadMediaOutput(
        String videoId,
        VideoMediaType mediaType
) {
    public static UploadMediaOutput of(final Video aVideo, final VideoMediaType aType) {
        return new UploadMediaOutput(aVideo.getId().getValue(), aType);
    }
}
