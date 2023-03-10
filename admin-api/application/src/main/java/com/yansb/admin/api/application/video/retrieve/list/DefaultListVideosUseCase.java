package com.yansb.admin.api.application.video.retrieve.list;

import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideosUseCase extends ListVideosUseCase {
  private final VideoGateway videoGateway;

  public DefaultListVideosUseCase(final VideoGateway videoGateway) {
    this.videoGateway = Objects.requireNonNull(videoGateway);
  }


  @Override
  public Pagination<VideoListOutput> execute(VideoSearchQuery aQuery) {
    return videoGateway.findAll(aQuery)
            .map(VideoListOutput::from);
  }
}
