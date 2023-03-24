package com.yansb.admin.api.infrastructure.video;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.video.*;
import com.yansb.admin.api.infrastructure.services.StorageService;
import com.yansb.admin.api.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.yansb.admin.api.domain.Fixture.Videos.mediaType;

@IntegrationTest
class DefaultMediaResourceGatewayTest {
    @Autowired
    private MediaResourceGateway mediaResourceGateway;
    @Autowired
    private StorageService storageService;

    @BeforeEach
    public void setUp() {
        storageService().reset();
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(mediaResourceGateway);
        Assertions.assertInstanceOf(DefaultMediaResourceGateway.class, mediaResourceGateway);

        Assertions.assertNotNull(storageService);
        Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
        //given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";


        //when
        final var actualMedia = this.mediaResourceGateway.storeAudioVideo(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        //then
        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.rawLocation());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualMedia.status());
        Assertions.assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

        final var actualResource = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
        //given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());


        //when
        final var actualMedia = this.mediaResourceGateway.storeImage(expectedVideoId, VideoResource.with(expectedResource, expectedType));

        //then
        Assertions.assertNotNull(actualMedia.id());
        Assertions.assertEquals(expectedLocation, actualMedia.location());
        Assertions.assertEquals(expectedResource.name(), actualMedia.name());
        Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());

        final var actualResource = storageService().storage().get(expectedLocation);

        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
        //given
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(id ->
                storageService.store(id, Fixture.Videos.resource(mediaType()))
        );
        expectedValues.forEach(id ->
                storageService.store(id, Fixture.Videos.resource(mediaType()))
        );

        Assertions.assertEquals(5, storageService().storage().size());
        //when
        this.mediaResourceGateway.clearResources(videoOne);

        //then
        Assertions.assertEquals(2, storageService().storage().size());
        final var actualKeys = storageService().storage().keySet();
        Assertions.assertTrue(expectedValues.size() == actualKeys.size()
                && expectedValues.containsAll(actualKeys)
                && actualKeys.containsAll(expectedValues));
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }
}