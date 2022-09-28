package com.yansb.admin.api.application.video.create;

import com.yansb.admin.api.application.Fixture;
import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.video.Resource;
import com.yansb.admin.api.domain.video.VideoGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateVideoUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultCreateVideoUseCase useCase;

  @Mock
  private VideoGateway videoGateway;

  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(videoGateway, categoryGateway, castMemberGateway, genreGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
    // given
    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLauchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.Videos.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.documentaries().getId());
    final var expectedGenres = Set.of(Fixture.Genres.action().getId());
    final var expectedMembers = Set.of(Fixture.CastMembers.yan().getId(), Fixture.CastMembers.gabriel().getId());

    final Resource expectedVideo = Fixture.Videos.resource(Resource.Type.VIDEO);
    final Resource expectedTrailer = Fixture.Videos.resource(Resource.Type.TRAILER);
    final Resource expectedBanner = Fixture.Videos.resource(Resource.Type.BANNER);
    final Resource expectedThumbnail = Fixture.Videos.resource(Resource.Type.THUMBNAIL);
    final Resource expectedThumbHalf = Fixture.Videos.resource(Resource.Type.THUMBNAIL_HALF);

    final var aCommand = CreateVideoCommand.with(
        expectedTitle,
        expectedDescription,
        expectedLauchYear,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumbnail,
        expectedThumbHalf
    );

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    when(videoGateway.create(any()))
        .thenAnswer(returnsFirstArg());
    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway).create(argThat(actualVideo ->
        Objects.equals(expectedTitle, actualVideo.getTitle())
            && Objects.equals(expectedDescription, actualVideo.getDescription())
            && Objects.equals(expectedLauchYear, actualVideo.getLaunchedAt())
            && Objects.equals(expectedDuration, actualVideo.getDuration())
            && Objects.equals(expectedOpened, actualVideo.getOpened())
            && Objects.equals(expectedPublished, actualVideo.getPublished())
            && Objects.equals(expectedRating, actualVideo.getRating())
            && Objects.equals(expectedCategories, actualVideo.getCategories())
            && Objects.equals(expectedGenres, actualVideo.getGenres())
            && Objects.equals(expectedMembers, actualVideo.getCastMembers())
            && actualVideo.getVideo().isPresent()
            && actualVideo.getTrailer().isPresent()
            && actualVideo.getBanner().isPresent()
            && actualVideo.getThumbnail().isPresent()
            && actualVideo.getThumbnailHalf().isPresent()
    ));
  }
}