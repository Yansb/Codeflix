package com.yansb.admin.api.application.video.delete;

import com.yansb.admin.api.domain.video.MediaResourceGateway;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {
  private final VideoGateway videoGateway;
  private final MediaResourceGateway mediaResourceGateway;

  public DefaultDeleteVideoUseCase(
          final VideoGateway videoGateway,
          final MediaResourceGateway mediaResourceGateway
  ) {
    this.videoGateway = Objects.requireNonNull(videoGateway);
    this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
  }


  @Override
  public void execute(String input) {
    final var aVideoId = VideoID.from(input);
    this.videoGateway.deleteById(aVideoId);
    this.mediaResourceGateway.clearResources(aVideoId);
  }
}