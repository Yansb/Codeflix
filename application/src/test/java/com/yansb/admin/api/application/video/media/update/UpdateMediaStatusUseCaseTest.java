package com.yansb.admin.api.application.video.media.update;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.video.MediaStatus;
import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateMediaStatusUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateMediaStatusUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenACommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        //given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFileName = "filename.mp4";
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.lordOfTheRings()
                .updateVideoMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        //when
        this.useCase.execute(aCmd);

        //then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1))
                .findById(eq(expectedId));
        verify(videoGateway, times(1))
                .update(captor.capture());

        final var actualVideo = captor.getValue();
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(
                expectedFolder.concat("/").concat(expectedFileName),
                actualVideoMedia.encodedLocation());
    }

    @Test
    public void givenACommandForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        //given
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFileName = null;
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.lordOfTheRings()
                .updateVideoMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        //when
        this.useCase.execute(aCmd);

        //then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1))
                .findById(eq(expectedId));
        verify(videoGateway, times(1))
                .update(captor.capture());

        final var actualVideo = captor.getValue();
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());

        final var actualVideoMedia = actualVideo.getVideo().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertTrue(actualVideoMedia.encodedLocation().isBlank());
    }

    @Test
    public void givenACommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
        //given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFileName = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.lordOfTheRings()
                .updateTrailerMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        //when
        this.useCase.execute(aCmd);

        //then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1))
                .findById(eq(expectedId));
        verify(videoGateway, times(1))
                .update(captor.capture());

        final var actualVideo = captor.getValue();
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertEquals(
                expectedFolder.concat("/").concat(expectedFileName),
                actualVideoMedia.encodedLocation());
    }

    @Test
    public void givenACommandForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
        //given
        final var expectedStatus = MediaStatus.PROCESSING;
        final String expectedFolder = null;
        final String expectedFileName = null;
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.lordOfTheRings()
                .updateTrailerMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                expectedMedia.id(),
                expectedFolder,
                expectedFileName
        );

        //when
        this.useCase.execute(aCmd);

        //then
        final var captor = ArgumentCaptor.forClass(Video.class);

        verify(videoGateway, times(1))
                .findById(eq(expectedId));
        verify(videoGateway, times(1))
                .update(captor.capture());

        final var actualVideo = captor.getValue();
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());

        final var actualVideoMedia = actualVideo.getTrailer().get();

        Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
        Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
        Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
        Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
        Assertions.assertTrue(actualVideoMedia.encodedLocation().isBlank());
    }

    @Test
    public void givenACommandForTrailer_whenIsInvalid_shouldDoNothing() {
        //given
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedFolder = "encoded_media";
        final var expectedFileName = "filename.mp4";
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        final var aVideo = Fixture.Videos.lordOfTheRings()
                .updateTrailerMedia(expectedMedia);

        final var expectedId = aVideo.getId();

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        final var aCmd = UpdateMediaStatusCommand.with(
                expectedStatus,
                expectedId.getValue(),
                "invalid_id",
                expectedFolder,
                expectedFileName
        );

        //when
        this.useCase.execute(aCmd);

        //then
        verify(videoGateway, times(0))
                .update(any());
    }


}