package com.yansb.admin.api.infrastructure.video.presenters;

import com.yansb.admin.api.application.video.retrieve.get.VideoOutput;
import com.yansb.admin.api.application.video.update.UpdateVideoOutput;
import com.yansb.admin.api.domain.video.AudioVideoMedia;
import com.yansb.admin.api.domain.video.ImageMedia;
import com.yansb.admin.api.infrastructure.video.models.AudioVideoMediaResponse;
import com.yansb.admin.api.infrastructure.video.models.ImageMediaResponse;
import com.yansb.admin.api.infrastructure.video.models.UpdateVideoResponse;
import com.yansb.admin.api.infrastructure.video.models.VideoResponse;

public interface VideoApiPresenter {
    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                present(output.video()),
                present(output.trailer()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia videoMedia) {
        if (videoMedia == null)
            return null;

        return new AudioVideoMediaResponse(
                videoMedia.id(),
                videoMedia.checksum(),
                videoMedia.name(),
                videoMedia.rawLocation(),
                videoMedia.encodedLocation(),
                videoMedia.status().name()
        );
    }

    static ImageMediaResponse present(final ImageMedia imageMedia) {
        if (imageMedia == null)
            return null;

        return new ImageMediaResponse(
                imageMedia.id(),
                imageMedia.checksum(),
                imageMedia.name(),
                imageMedia.location()
        );
    }

    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoResponse(output.id());
    }
}
