package com.yansb.admin.api.domain.video;

public interface MediaResourceGateway {
    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);

    ImageMedia storeImage(VideoID anId, VideoResource aResource);

    void clearResources(VideoID anId);

}
