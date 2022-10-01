package com.yansb.admin.api.application.video.retrieve.get;

import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {

  private VideoGateway videoGateway;

  public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
    this.videoGateway = Objects.requireNonNull(videoGateway);
  }

  @Override
  public VideoOutput execute(String anInput) {
    final var aVideoId = VideoID.from(anInput);

    return this.videoGateway.findById(aVideoId).
        map(VideoOutput::from)
        .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
  }
}
