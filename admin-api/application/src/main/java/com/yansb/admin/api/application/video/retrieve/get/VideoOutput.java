package com.yansb.admin.api.application.video.retrieve.get;

import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.utils.CollectionUtils;
import com.yansb.admin.api.domain.video.AudioVideoMedia;
import com.yansb.admin.api.domain.video.ImageMedia;
import com.yansb.admin.api.domain.video.Rating;
import com.yansb.admin.api.domain.video.Video;

import java.time.Instant;
import java.util.Set;

public record VideoOutput(
    String id,
    Instant createdAt,
    Instant updatedAt,
    String title,
    String description,
    int launchedAt,
    double duration,
    boolean opened,
    boolean published,
    Rating rating,
    Set<String> categories,
    Set<String> genres,
    Set<String> castMembers,
    AudioVideoMedia video,
    AudioVideoMedia trailer,
    ImageMedia banner,
    ImageMedia thumbnail,
    ImageMedia thumbnailHalf
) {
  public static VideoOutput from(final Video aVideo) {
    return new VideoOutput(
        aVideo.getId().getValue(),
        aVideo.getCreatedAt(),
        aVideo.getUpdatedAt(),
        aVideo.getTitle(),
        aVideo.getDescription(),
        aVideo.getLaunchedAt().getValue(),
        aVideo.getDuration(),
        aVideo.getOpened(),
        aVideo.getPublished(),
        aVideo.getRating(),
        CollectionUtils.mapTo(aVideo.getCategories(), CategoryID::getValue),
        CollectionUtils.mapTo(aVideo.getGenres(), GenreID::getValue),
        CollectionUtils.mapTo(aVideo.getCastMembers(), CastMemberID::getValue),
        aVideo.getVideo().orElse(null),
        aVideo.getTrailer().orElse(null),
        aVideo.getBanner().orElse(null),
        aVideo.getThumbnail().orElse(null),
        aVideo.getThumbnailHalf().orElse(null)
    );

  }
}
