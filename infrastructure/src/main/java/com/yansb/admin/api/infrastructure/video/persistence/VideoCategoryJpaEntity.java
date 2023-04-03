package com.yansb.admin.api.infrastructure.video.persistence;

import com.yansb.admin.api.domain.category.CategoryID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "VideoCategory")
@Table(name = "videos_categories")
public class VideoCategoryJpaEntity {

    @EmbeddedId
    private VideoCategoryID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCategoryJpaEntity() {
    }

    private VideoCategoryJpaEntity(final VideoCategoryID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCategoryJpaEntity from(final VideoJpaEntity video, CategoryID category) {
        return new VideoCategoryJpaEntity(VideoCategoryID.from(video.getId(), category.getValue()),
                video
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(video, that.video);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, video);
    }

    public VideoCategoryID getId() {
        return id;
    }

    public void setId(VideoCategoryID id) {
        this.id = id;
    }

    public com.yansb.admin.api.infrastructure.video.persistence.VideoJpaEntity getVideo() {
        return video;
    }

    public void setVideo(com.yansb.admin.api.infrastructure.video.persistence.VideoJpaEntity video) {
        this.video = video;
    }
}
