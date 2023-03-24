package com.yansb.admin.api.infrastructure.video;

import com.yansb.admin.api.domain.video.*;
import com.yansb.admin.api.infrastructure.configuration.properties.storage.StorageProperties;
import com.yansb.admin.api.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String fileNamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
        this.fileNamePattern = props.getFileNamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var aResource = videoResource.resource();
        store(filePath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filePath);
    }


    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
        final var filePath = filePath(anId, videoResource);
        final var aResource = videoResource.resource();
        store(filePath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(), filePath);
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = this.storageService.list(folder(anId));
        this.storageService.deleteAll(ids);
    }

    private void store(String filePath, Resource aResource) {
        this.storageService.store(filePath, aResource);
    }

    private String filename(final VideoMediaType aType) {
        return fileNamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filePath(final VideoID anId, final VideoResource aResource) {
        return folder(anId)
                .concat("/")
                .concat(filename(aResource.type()));
    }

}
