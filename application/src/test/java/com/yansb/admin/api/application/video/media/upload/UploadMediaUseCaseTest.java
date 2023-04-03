package com.yansb.admin.api.application.video.media.upload;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.video.MediaResourceGateway;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoMediaType;
import com.yansb.admin.api.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUploadMediaUseCase useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return null;
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateVideoMediaAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.lordOfTheRings();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeAudioVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when
        final var actualOutput = useCase.execute(aCmd);
        //then

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1))
                .storeAudioVideo(eq(expectedId), eq(expectedVideoResource));
        verify(videoGateway, times(1))
                .update(argThat(actualVideo ->
                        Objects.equals(expectedMedia, actualVideo.getVideo().get())
                                && actualVideo.getTrailer().isEmpty()
                                && actualVideo.getBanner().isEmpty()
                                && actualVideo.getThumbnail().isEmpty()
                                && actualVideo.getThumbnailHalf().isEmpty()
                ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.lordOfTheRings();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeAudioVideo(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when
        final var actualOutput = useCase.execute(aCmd);
        //then

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1))
                .storeAudioVideo(eq(expectedId), eq(expectedVideoResource));
        verify(videoGateway, times(1))
                .update(argThat(actualVideo ->
                        Objects.equals(expectedMedia, actualVideo.getTrailer().get())
                                && actualVideo.getVideo().isEmpty()
                                && actualVideo.getBanner().isEmpty()
                                && actualVideo.getThumbnail().isEmpty()
                                && actualVideo.getThumbnailHalf().isEmpty()
                ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateBannerAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.lordOfTheRings();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when
        final var actualOutput = useCase.execute(aCmd);
        //then

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1))
                .storeImage(eq(expectedId), eq(expectedVideoResource));
        verify(videoGateway, times(1))
                .update(argThat(actualVideo ->
                        Objects.equals(expectedMedia, actualVideo.getBanner().get())
                                && actualVideo.getTrailer().isEmpty()
                                && actualVideo.getVideo().isEmpty()
                                && actualVideo.getThumbnail().isEmpty()
                                && actualVideo.getThumbnailHalf().isEmpty()
                ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.lordOfTheRings();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when
        final var actualOutput = useCase.execute(aCmd);
        //then

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1))
                .storeImage(eq(expectedId), eq(expectedVideoResource));
        verify(videoGateway, times(1))
                .update(argThat(actualVideo ->
                        Objects.equals(expectedMedia, actualVideo.getThumbnail().get())
                                && actualVideo.getTrailer().isEmpty()
                                && actualVideo.getBanner().isEmpty()
                                && actualVideo.getVideo().isEmpty()
                                && actualVideo.getThumbnailHalf().isEmpty()
                ));
    }

    @Test
    public void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailHalfAndPersistIt() {
        //given
        final var aVideo = Fixture.Videos.lordOfTheRings();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(videoGateway.findById(any()))
                .thenReturn(Optional.of(aVideo));

        when(mediaResourceGateway.storeImage(any(), any()))
                .thenReturn(expectedMedia);

        when(videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when
        final var actualOutput = useCase.execute(aCmd);
        //then

        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(videoGateway, times(1)).findById(eq(expectedId));
        verify(mediaResourceGateway, times(1))
                .storeImage(eq(expectedId), eq(expectedVideoResource));
        verify(videoGateway, times(1))
                .update(argThat(actualVideo ->
                        Objects.equals(expectedMedia, actualVideo.getThumbnailHalf().get())
                                && actualVideo.getTrailer().isEmpty()
                                && actualVideo.getBanner().isEmpty()
                                && actualVideo.getThumbnail().isEmpty()
                                && actualVideo.getVideo().isEmpty()
                ));
    }

    @Test
    public void givenCmdToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        //given
        final var aVideo = Fixture.Videos.lordOfTheRings();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);
        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> useCase.execute(aCmd));
        //then

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}