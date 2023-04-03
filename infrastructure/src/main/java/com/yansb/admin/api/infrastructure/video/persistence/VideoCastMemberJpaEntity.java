package com.yansb.admin.api.infrastructure.video.persistence;

import com.yansb.admin.api.domain.castMember.CastMemberID;

import javax.persistence.*;

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
public class VideoCastMemberJpaEntity {

    @EmbeddedId
    private VideoCastMemberId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCastMemberJpaEntity() {
    }

    private VideoCastMemberJpaEntity(VideoCastMemberId id, VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCastMemberJpaEntity from(final VideoJpaEntity entity, final CastMemberID castMemberID) {
        return new VideoCastMemberJpaEntity(VideoCastMemberId.from(
                entity.getId(), castMemberID.getValue()),
                entity
        );
    }

    public VideoCastMemberId getId() {
        return id;
    }

    public void setId(VideoCastMemberId id) {
        this.id = id;
    }

    public VideoJpaEntity getVideo() {
        return video;
    }

    public void setVideo(VideoJpaEntity video) {
        this.video = video;
    }
}
