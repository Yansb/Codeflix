package com.yansb.admin.api.domain.video;

import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

public class VideoTest {
  @Test
  public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());

    // when
    final var actualVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());
    Assertions.assertNotNull(actualVideo.getCreatedAt());
    Assertions.assertNotNull(actualVideo.getUpdatedAt());
    Assertions.assertEquals(actualVideo.getCreatedAt(), actualVideo.getUpdatedAt());
    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertTrue(actualVideo.getVideo().isEmpty());
    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
    Assertions.assertTrue(actualVideo.getBanner().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var aVideo = Video.newVideo(
        "wrong title",
        "wrong description",
        Year.of(2025),
        0.0,
        true,
        true,
        Rating.ER,
        Set.of(),
        Set.of(),
        Set.of()
    );

    // when
    final var actualVideo = Video.with(aVideo).update(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());
    Assertions.assertEquals(actualVideo.getCreatedAt(), aVideo.getCreatedAt());
    Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertTrue(actualVideo.getVideo().isEmpty());
    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
    Assertions.assertTrue(actualVideo.getBanner().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsSetVideo_shouldReturnUpdated() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var aVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    final var aVideoMedia = AudioVideoMedia.with("abc", "Video.mp4", "/123/videos", "", MediaStatus.PENDING);
    // when
    final var actualVideo = Video.with(aVideo).setVideo(aVideoMedia);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());
    Assertions.assertEquals(actualVideo.getCreatedAt(), aVideo.getCreatedAt());
    Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertTrue(aVideoMedia.equals(actualVideo.getVideo().get()));
    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
    Assertions.assertTrue(actualVideo.getBanner().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsSetTrailer_shouldReturnUpdated() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var aVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    final var aTrailerMedia = AudioVideoMedia.with("abc", "trailer.mp4", "/123/trailers", "", MediaStatus.PENDING);
    // when
    final var actualVideo = Video.with(aVideo).setTrailer(aTrailerMedia);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());
    Assertions.assertEquals(actualVideo.getCreatedAt(), aVideo.getCreatedAt());
    Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertTrue(aTrailerMedia.equals(actualVideo.getTrailer().get()));
    Assertions.assertTrue(actualVideo.getVideo().isEmpty());
    Assertions.assertTrue(actualVideo.getBanner().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsSetBanner_shouldReturnUpdated() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var aVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    final var aBannerMedia = ImageMedia.with("abc", "trailer.mp4", "/123/trailers");
    // when
    final var actualVideo = Video.with(aVideo).setBanner(aBannerMedia);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());
    Assertions.assertEquals(actualVideo.getCreatedAt(), aVideo.getCreatedAt());
    Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertEquals(actualVideo.getBanner().get(), aBannerMedia);
    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
    Assertions.assertTrue(actualVideo.getVideo().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsSetThumbnail_shouldReturnUpdated() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var aVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    final var aThumbMedia = ImageMedia.with("abc", "trailer.mp4", "/123/trailers");
    // when
    final var actualVideo = Video.with(aVideo).setThumbnail(aThumbMedia);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());
    Assertions.assertEquals(actualVideo.getCreatedAt(), aVideo.getCreatedAt());
    Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertEquals(actualVideo.getThumbnail().get(), aThumbMedia);
    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
    Assertions.assertTrue(actualVideo.getVideo().isEmpty());
    Assertions.assertTrue(actualVideo.getBanner().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsSetThumbnailHalf_shouldReturnUpdated() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription = """
        This course is designed to help you prepare for system design interviews.
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var aVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchedAt,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    final var aThumb = ImageMedia.with("abc", "trailer.mp4", "/123/trailers");
    // when
    final var actualVideo = Video.with(aVideo).setThumbnailHalf(aThumb);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());
    Assertions.assertEquals(actualVideo.getCreatedAt(), aVideo.getCreatedAt());
    Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertEquals(actualVideo.getThumbnailHalf().get(), aThumb);
    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
    Assertions.assertTrue(actualVideo.getVideo().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
    Assertions.assertTrue(actualVideo.getBanner().isEmpty());

    Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }
}
