package com.yansb.admin.api.infrastructure.services.local;

import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.utils.IdUtils;
import com.yansb.admin.api.domain.video.VideoMediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryStorageServiceTest {
    private final InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setup() {
        this.target.reset();
    }

    @Test
    public void givenValidResource_whenCallsStore_shouldStoreIt() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);

        //when
        target.store(expectedName, expectedResource);

        //then
        assertEquals(expectedResource, target.storage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallsGet_shouldRetrieveIt() {
        //given
        final var expectedName = IdUtils.uuid();
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.VIDEO);
        target.store(expectedName, expectedResource);

        //when
        final var actualResource = target.get(expectedName).get();

        //then
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_shouldBeEmpty() {
        //given
        final var expectedName = IdUtils.uuid();

        //when
        final var actualResource = target.get(expectedName);

        //then
        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsStore_shouldStoreIt() {
        //given
        final var expectedNames = List.of(
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid()
        );
        final var all = new ArrayList<>(expectedNames);

        all.add("other_" + IdUtils.uuid());
        all.add("other_" + IdUtils.uuid());

        all.forEach(it -> target.store(it, Fixture.Videos.resource(VideoMediaType.VIDEO)));
        assertEquals(all.size(), target.storage().size());

        //when
        final var actualResource = target.list("video_");

        //then
        assertTrue(
                expectedNames.size() == actualResource.size()
                        && expectedNames.containsAll(actualResource)
        );
    }

    @Test
    public void givenValidNames_whenCallsDelete_shouldDeleteAll() {
        //given
        final var videos = List.of(
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid(),
                "video_" + IdUtils.uuid()
        );
        final var expectedNames = Set.of(
                "other_" + IdUtils.uuid(),
                "other_" + IdUtils.uuid()
        );
        final var all = new ArrayList<>(videos);
        all.addAll(expectedNames);

        all.forEach(it -> target.store(it, Fixture.Videos.resource(VideoMediaType.VIDEO)));
        assertEquals(all.size(), target.storage().size());

        //when
        target.deleteAll(videos);

        //then
        assertEquals(2, target.storage().size());
        final var actualKeys = target.storage().keySet();
        assertTrue(
                expectedNames.size() == actualKeys.size()
                        && expectedNames.containsAll(actualKeys)
        );
    }
}