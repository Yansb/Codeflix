package com.yansb.admin.api.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCastMemberId implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "cast_member_id", nullable = false)
    private String castMemberId;

    public VideoCastMemberId() {

    }

    public VideoCastMemberId(String videoId, String castMemberId) {
        this.videoId = videoId;
        this.castMemberId = castMemberId;
    }

    public static VideoCastMemberId from(final String videoId, final String castMemberId) {
        return new VideoCastMemberId(videoId, castMemberId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCastMemberId that = (VideoCastMemberId) o;
        return Objects.equals(videoId, that.videoId) && Objects.equals(castMemberId, that.castMemberId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(videoId, castMemberId);
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCastMemberId() {
        return castMemberId;
    }

    public void setCastMemberId(String castMemberId) {
        this.castMemberId = castMemberId;
    }
}
