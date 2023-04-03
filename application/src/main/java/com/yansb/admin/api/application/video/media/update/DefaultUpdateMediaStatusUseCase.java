package com.yansb.admin.api.application.video.media.update;

import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.video.*;

import java.util.Objects;

import static com.yansb.admin.api.domain.video.VideoMediaType.TRAILER;
import static com.yansb.admin.api.domain.video.VideoMediaType.VIDEO;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {
    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(UpdateMediaStatusCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aResourceId = aCmd.resourceId();
        final var folder = aCmd.folder();
        final var filename = aCmd.filename();

        final var aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> notFound(anId));

        final var encodedPath = "%s/%s".formatted(folder, filename);

        if (matches(aResourceId, aVideo.getVideo().orElse(null))) {
            updateVideo(VIDEO, aCmd.status(), aVideo, encodedPath);
        } else if (matches(aResourceId, aVideo.getTrailer().orElse(null))) {
            updateVideo(TRAILER, aCmd.status(), aVideo, encodedPath);
        }
    }

    private void updateVideo(final VideoMediaType aType, MediaStatus aStatus, Video aVideo, String encodedPath) {
        switch (aStatus) {
            case PENDING -> {
            }
            case PROCESSING -> aVideo.procesing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);

        }
        this.videoGateway.update(aVideo);
    }

    private boolean matches(final String resourceId, final AudioVideoMedia aMedia) {
        if (aMedia == null) {
            return false;
        }

        return aMedia.id().equals(resourceId);
    }

    private NotFoundException notFound(VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
