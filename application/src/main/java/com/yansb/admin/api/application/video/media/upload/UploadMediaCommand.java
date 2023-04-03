package com.yansb.admin.api.application.video.media.upload;

import com.yansb.admin.api.domain.video.VideoResource;

public record UploadMediaCommand(
        String videoId,
        VideoResource videoResource
) {
    public static UploadMediaCommand with(final String aVideoId, final VideoResource aVideoResource) {
        return new UploadMediaCommand(aVideoId, aVideoResource);
    }
}
