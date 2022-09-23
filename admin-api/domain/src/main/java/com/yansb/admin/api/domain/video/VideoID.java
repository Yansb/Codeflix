package com.yansb.admin.api.domain.video;

import com.yansb.admin.api.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {
  private final String value;

  private VideoID(final String anId) {
    this.value = Objects.requireNonNull(anId);
  }


  public static VideoID from(final String anID) {
    return new VideoID(anID.toLowerCase());
  }

  public static VideoID from(final UUID anId) {
    return VideoID.from(anId.toString());
  }

  public static VideoID unique() {
    return VideoID.from(UUID.randomUUID());
  }

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final VideoID that = (VideoID) o;
    return getValue().equals(that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }
}
