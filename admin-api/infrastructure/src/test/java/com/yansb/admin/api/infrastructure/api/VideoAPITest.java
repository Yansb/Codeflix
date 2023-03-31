package com.yansb.admin.api.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yansb.admin.api.ControllerTest;
import com.yansb.admin.api.application.video.create.CreateVideoCommand;
import com.yansb.admin.api.application.video.create.CreateVideoOutput;
import com.yansb.admin.api.application.video.create.CreateVideoUseCase;
import com.yansb.admin.api.application.video.retrieve.get.GetVideoByIdUseCase;
import com.yansb.admin.api.application.video.retrieve.get.VideoOutput;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoID;
import com.yansb.admin.api.domain.video.VideoMediaType;
import com.yansb.admin.api.infrastructure.video.models.CreateVideoRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Year;
import java.util.ArrayList;
import java.util.Set;

import static com.yansb.admin.api.domain.utils.CollectionUtils.mapTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
class VideoAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateFull_shouldReturnAnId() throws Exception {
        //given
        final var yan = Fixture.CastMembers.yan();
        final var documentaries = Fixture.Categories.documentaries();
        final var action = Fixture.Genres.action();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentaries.getId().getValue());
        final var expectedGenres = Set.of(action.getId().getValue());
        final var expectedMembers = Set.of(yan.getId().getValue());

        final var expectedVideo =
                new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
        final var expectedTrailer =
                new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());
        final var expectedBanner =
                new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());
        final var expectedThumbnail =
                new MockMultipartFile("thumb_file", "thumbnail.jpg", "image/jpg", "THUMB".getBytes());
        final var expectedThumbHalf =
                new MockMultipartFile("thumb_half_file", "thumbnailHalf.mp4", "image/jpg", "THUMBHALF".getBytes());

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));
        //when
        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumbnail)
                .file(expectedThumbHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear))
                .param("rating", expectedRating.getName())
                .param("duration", expectedDuration.toString())
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("cast_members_id", yan.getId().getValue())
                .param("categories_id", documentaries.getId().getValue())
                .param("genres_id", action.getId().getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        //then
        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear, actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedMembers, actualCmd.members());
        Assertions.assertEquals(expectedVideo.getOriginalFilename(), actualCmd.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.getOriginalFilename(), actualCmd.getBanner().get().name());
        Assertions.assertEquals(expectedThumbnail.getOriginalFilename(), actualCmd.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.getOriginalFilename(), actualCmd.getThumbnailHalf().get().name());
    }

    @Test
    public void givenAValidCommand_whenCallsCreatePartial_shouldReturnId() throws Exception {
        //given
        final var yan = Fixture.CastMembers.yan();
        final var documentaries = Fixture.Categories.documentaries();
        final var action = Fixture.Genres.action();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Fixture.year();
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentaries.getId().getValue());
        final var expectedGenres = Set.of(action.getId().getValue());
        final var expectedMembers = Set.of(yan.getId().getValue());

        final var aCommand = new CreateVideoRequest(
                expectedTitle,
                expectedDescription,
                expectedDuration,
                expectedLaunchYear,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedMembers,
                expectedCategories,
                expectedGenres
        );

        when(createVideoUseCase.execute(any()))
                .thenReturn(new CreateVideoOutput(expectedId.getValue()));
        //when

        final var aRequest = post("/videos")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        this.mvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/videos/" + expectedId.getValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId.getValue())));

        //then

        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);

        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();

        Assertions.assertEquals(expectedTitle, actualCmd.title());
        Assertions.assertEquals(expectedDescription, actualCmd.description());
        Assertions.assertEquals(expectedLaunchYear, actualCmd.launchedAt());
        Assertions.assertEquals(expectedDuration, actualCmd.duration());
        Assertions.assertEquals(expectedOpened, actualCmd.opened());
        Assertions.assertEquals(expectedPublished, actualCmd.published());
        Assertions.assertEquals(expectedRating.getName(), actualCmd.rating());
        Assertions.assertEquals(expectedCategories, actualCmd.categories());
        Assertions.assertEquals(expectedGenres, actualCmd.genres());
        Assertions.assertEquals(expectedMembers, actualCmd.members());
        Assertions.assertTrue(actualCmd.getVideo().isEmpty());
        Assertions.assertTrue(actualCmd.getTrailer().isEmpty());
        Assertions.assertTrue(actualCmd.getBanner().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnail().isEmpty());
        Assertions.assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAValidId_whenCallsGetById_thenShouldReturnVideo() throws Exception {
        //given
        final var yan = Fixture.CastMembers.yan();
        final var documentaries = Fixture.Categories.documentaries();
        final var action = Fixture.Genres.action();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.Videos.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(documentaries.getId().getValue());
        final var expectedGenres = Set.of(action.getId().getValue());
        final var expectedMembers = Set.of(yan.getId().getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        mapTo(expectedCategories, CategoryID::from),
                        mapTo(expectedGenres, GenreID::from),
                        mapTo(expectedMembers, CastMemberID::from)
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any()))
                .thenReturn(VideoOutput.from(aVideo));
        //when
        final var aRequest = MockMvcRequestBuilders.get("/videos/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(jsonPath("$.title", Matchers.equalTo(expectedTitle)))
                .andExpect(jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(jsonPath("$.year_launched", Matchers.equalTo(expectedLaunchYear.getValue())))
                .andExpect(jsonPath("$.duration", Matchers.equalTo(expectedDuration)))
                .andExpect(jsonPath("$.opened", Matchers.equalTo(expectedOpened)))
                .andExpect(jsonPath("$.published", Matchers.equalTo(expectedPublished)))
                .andExpect(jsonPath("$.rating", Matchers.equalTo(expectedRating.getName())))
                .andExpect(jsonPath("$.created_at", Matchers.equalTo(aVideo.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", Matchers.equalTo(aVideo.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.banner.id", Matchers.equalTo(expectedBanner.id())))
                .andExpect(jsonPath("$.banner.name", Matchers.equalTo(expectedBanner.name())))
                .andExpect(jsonPath("$.banner.location", Matchers.equalTo(expectedBanner.location())))
                .andExpect(jsonPath("$.banner.checksum", Matchers.equalTo(expectedBanner.checksum())))
                .andExpect(jsonPath("$.thumbnail.id", Matchers.equalTo(expectedThumb.id())))
                .andExpect(jsonPath("$.thumbnail.name", Matchers.equalTo(expectedThumb.name())))
                .andExpect(jsonPath("$.thumbnail.location", Matchers.equalTo(expectedThumb.location())))
                .andExpect(jsonPath("$.thumbnail.checksum", Matchers.equalTo(expectedThumb.checksum())))
                .andExpect(jsonPath("$.thumbnail_half.id", Matchers.equalTo(expectedThumbHalf.id())))
                .andExpect(jsonPath("$.thumbnail_half.name", Matchers.equalTo(expectedThumbHalf.name())))
                .andExpect(jsonPath("$.thumbnail_half.location", Matchers.equalTo(expectedThumbHalf.location())))
                .andExpect(jsonPath("$.thumbnail_half.checksum", Matchers.equalTo(expectedThumbHalf.checksum())))
                .andExpect(jsonPath("$.video.id", Matchers.equalTo(expectedVideo.id())))
                .andExpect(jsonPath("$.video.name", Matchers.equalTo(expectedVideo.name())))
                .andExpect(jsonPath("$.video.checksum", Matchers.equalTo(expectedVideo.checksum())))
                .andExpect(jsonPath("$.video.location", Matchers.equalTo(expectedVideo.rawLocation())))
                .andExpect(jsonPath("$.video.encoded_location", Matchers.equalTo(expectedVideo.encodedLocation())))
                .andExpect(jsonPath("$.video.status", Matchers.equalTo(expectedVideo.status().name())))
                .andExpect(jsonPath("$.trailer.id", Matchers.equalTo(expectedTrailer.id())))
                .andExpect(jsonPath("$.trailer.name", Matchers.equalTo(expectedTrailer.name())))
                .andExpect(jsonPath("$.trailer.checksum", Matchers.equalTo(expectedTrailer.checksum())))
                .andExpect(jsonPath("$.trailer.location", Matchers.equalTo(expectedTrailer.rawLocation())))
                .andExpect(jsonPath("$.trailer.encoded_location", Matchers.equalTo(expectedTrailer.encodedLocation())))
                .andExpect(jsonPath("$.trailer.status", Matchers.equalTo(expectedTrailer.status().name())))
                .andExpect(jsonPath("$.categories_id", Matchers.equalTo(new ArrayList(expectedCategories))))
                .andExpect(jsonPath("$.genres_id", Matchers.equalTo(new ArrayList(expectedGenres))))
                .andExpect(jsonPath("$.cast_members_id", Matchers.equalTo(new ArrayList(expectedMembers))));

    }
}