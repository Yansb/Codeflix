package com.yansb.admin.api.application.video.update;

import com.yansb.admin.api.domain.video.Video;

public record UpdateVideoOutput(String id) {
  public static UpdateVideoOutput from(final Video aVideo) {
    return new UpdateVideoOutput(aVideo.getId().getValue());
  }
}
