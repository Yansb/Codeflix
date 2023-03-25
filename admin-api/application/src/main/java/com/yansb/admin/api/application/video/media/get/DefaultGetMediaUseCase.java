package com.yansb.admin.api.application.video.media.get;

import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.video.MediaResourceGateway;
import com.yansb.admin.api.domain.video.VideoID;
import com.yansb.admin.api.domain.video.VideoMediaType;

import java.util.Objects;

public class DefaultGetMediaUseCase extends GetMediaUseCase {

    private final MediaResourceGateway mediaResourceGateway;

    public DefaultGetMediaUseCase(MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(GetMediaCommand aCmd) {
        final var anId = VideoID.from(aCmd.videoId());
        final var aType = VideoMediaType.of(aCmd.mediaType())
                .orElseThrow(() -> typeNotFound(aCmd.mediaType()));

        final var aResource =
                this.mediaResourceGateway.getResource(anId, aType)
                        .orElseThrow(() -> notFound(aCmd.videoId(), aCmd.mediaType()));

        return MediaOutput.with(aResource);
    }

    private NotFoundException notFound(final String anId, final String aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));

    }

    private NotFoundException typeNotFound(final String aType) {
        return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(aType)));
    }
}
