package com.yansb.admin.api.application.video.retrieve.get;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoID;
import com.yansb.admin.api.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.yansb.admin.api.domain.Fixture.Videos.audioVideo;
import static com.yansb.admin.api.domain.Fixture.Videos.image;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GetVideoByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetVideoByIdUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetVideo_shouldReturnIt() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLauchYear = Fixture.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.documentaries().getId());
        final var expectedGenres = Set.of(Fixture.Genres.action().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.yan().getId(), Fixture.CastMembers.gabriel().getId());

        final var expectedVideo = audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = image(VideoMediaType.BANNER);
        final var expectedThumbnail = image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        Year.of(expectedLauchYear),
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumbnail)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId().getValue();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(Video.with(aVideo)));
        // when
        final var actualVideo = useCase.execute(expectedId);

        // then
        Assertions.assertEquals(expectedId, actualVideo.id());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
        Assertions.assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());
        Assertions.assertEquals(expectedTitle, actualVideo.title());
        Assertions.assertEquals(expectedDescription, actualVideo.description());
        Assertions.assertEquals(expectedLauchYear, actualVideo.launchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.duration());
        Assertions.assertEquals(expectedOpened, actualVideo.opened());
        Assertions.assertEquals(expectedPublished, actualVideo.published());
        Assertions.assertEquals(expectedRating, actualVideo.rating());
        Assertions.assertEquals(asString(expectedCategories), actualVideo.categories());
        Assertions.assertEquals(asString(expectedGenres), actualVideo.genres());
        Assertions.assertEquals(asString(expectedMembers), actualVideo.castMembers());
        Assertions.assertEquals(expectedVideo, actualVideo.video());
        Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
        Assertions.assertEquals(expectedBanner, actualVideo.banner());
        Assertions.assertEquals(expectedThumbnail, actualVideo.thumbnail());
        Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbnailHalf());
    }

    @Test
    public void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Video with ID invalid-id was not found";
        final var expectedId = VideoID.from("invalid-id");

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        // when
        final var actualError = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(expectedId.getValue()));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualError.getMessage());
    }
}
