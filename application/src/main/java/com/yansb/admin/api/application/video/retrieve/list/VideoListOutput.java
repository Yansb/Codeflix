package com.yansb.admin.api.application.video.retrieve.list;

import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(
    String id,
    String title,
    String description,
    Instant createdAt,
    Instant updatedAt
) {

  public static VideoListOutput from(final Video video) {
    return new VideoListOutput(
        video.getId().getValue(),
        video.getTitle(),
        video.getDescription(),
        video.getCreatedAt(),
        video.getUpdatedAt()
    );
  }

  public static VideoListOutput from(final VideoPreview video) {
    return new VideoListOutput(
            video.id(),
            video.title(),
            video.description(),
            video.createdAt(),
            video.updatedAt()
    );
  }
}
