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
}
