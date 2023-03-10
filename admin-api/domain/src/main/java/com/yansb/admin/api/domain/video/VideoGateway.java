package com.yansb.admin.api.domain.video;

import com.yansb.admin.api.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {
  Video create(Video aVideo);

  Video update(Video aVideo);

  void deleteById(VideoID aVideoId);

  Optional<Video> findById(VideoID aVideoId);

  Pagination<VideoPreview> findAll(VideoSearchQuery aQuery);
}
