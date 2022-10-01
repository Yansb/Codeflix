package com.yansb.admin.api.application.video.delete;

import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {
  private final VideoGateway videoGateway;

  public DefaultDeleteVideoUseCase(final VideoGateway videoGateway) {
    this.videoGateway = Objects.requireNonNull(videoGateway);
  }


  @Override
  public void execute(String input) {
    this.videoGateway.deleteById(VideoID.from(input));
  }
}