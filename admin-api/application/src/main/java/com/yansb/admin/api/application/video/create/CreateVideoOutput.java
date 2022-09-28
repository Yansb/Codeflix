package com.yansb.admin.api.application.video.create;

import com.yansb.admin.api.domain.video.Video;

public record CreateVideoOutput(String id) {
  public static CreateVideoOutput with(final Video aVideo) {
    return new CreateVideoOutput(aVideo.getId().getValue());
  }
}
