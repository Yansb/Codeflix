package com.yansb.admin.api.infrastructure.services.impl;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.video.Resource;
import com.yansb.admin.api.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static com.google.cloud.storage.Storage.BlobListOption.prefix;
import static org.mockito.Mockito.*;

class GCStorageServiceTest {

    private final String bucket = "test";
    private GCStorageService target;
    private Storage storage;

    @BeforeEach
    public void setUp() {
        this.storage = Mockito.mock(Storage.class);
        this.target = new GCStorageService(bucket, storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).create(any(BlobInfo.class), any());

        this.target.store(expectedId, expectedResource);

        final var capturer = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1)).create(capturer.capture(), eq(expectedResource.content()));

        final var actualBlob = capturer.getValue();
        Assertions.assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedId, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
        Assertions.assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedName = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).get(anyString(), anyString());

        //when
        final var actualResource = this.target.get(expectedName).get();


        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldBeEmpty() {
        final var expectedName = Fixture.name();

        doReturn(null).when(storage).get(anyString(), anyString());

        //when
        final var actualResource = this.target.get(expectedName);


        verify(storage, times(1)).get(eq(this.bucket), eq(expectedName));

        Assertions.assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsStore_shouldStoreIt() {
        final var expectedPrefix = "media_";

        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);

        final var expectedNameVideo = expectedVideo.name();
        final var expectedNameBanner = expectedBanner.name();

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);
        final Blob blobVideo = mockBlob(expectedVideo);
        final var blobBanner = mockBlob(expectedBanner);

        final var page = Mockito.mock(Page.class);

        doReturn(List.of(blobVideo, blobBanner)).when(page).iterateAll();
        doReturn(page).when(storage).list(anyString(), any());

        //when
        final var actualResources = this.target.list(expectedPrefix);


        verify(storage, times(1)).list(eq(this.bucket), eq(prefix(expectedPrefix)));

        Assertions.assertTrue(expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources));
    }

    @Test
    public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
        final var expectedPrefix = "media_";

        final var expectedVideo = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var expectedBanner = Fixture.Videos.resource(VideoMediaType.BANNER);

        final var expectedNameVideo = expectedPrefix + expectedVideo.name();
        final var expectedNameBanner = expectedPrefix + expectedBanner.name();

        final var expectedResources = List.of(expectedNameVideo, expectedNameBanner);

        //when
        this.target.deleteAll(expectedResources);

        //then
        final var captor = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(captor.capture());

        final var actualResources = ((List<BlobId>) captor.getValue()).stream()
                .map(BlobId::getName)
                .toList();
        Assertions.assertTrue(expectedResources.size() == actualResources.size()
                && expectedResources.containsAll(actualResources)
                && actualResources.containsAll(expectedResources));
    }

    private Blob mockBlob(final Resource resource) {
        final var blob1 = Mockito.mock(Blob.class);
        when(blob1.getBlobId()).thenReturn(BlobId.of(bucket, resource.name()));
        when(blob1.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(blob1.getContent()).thenReturn(resource.content());
        when(blob1.getContentType()).thenReturn(resource.contentType());
        when(blob1.getName()).thenReturn(resource.name());
        return blob1;
    }

}