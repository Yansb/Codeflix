package com.yansb.admin.api.application.video.media.upload;

import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.video.MediaResourceGateway;
import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoID;

import java.util.Objects;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUploadMediaUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aResource = aCmd.videoResource();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notFound(anId));

        switch (aResource.type()) {
            case VIDEO -> aVideo.setVideo(this.mediaResourceGateway.storeAudioVideo(anId, aResource));
            case TRAILER -> aVideo.setTrailer(this.mediaResourceGateway.storeAudioVideo(anId, aResource));
            case BANNER -> aVideo.setBanner(this.mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL -> aVideo.setThumbnail(this.mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL_HALF -> aVideo.setThumbnailHalf(this.mediaResourceGateway.storeImage(anId, aResource));
        }

        return UploadMediaOutput.of(videoGateway.update(aVideo), aResource.type());
    }

    private NotFoundException notFound(VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
