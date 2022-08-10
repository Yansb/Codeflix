package com.yansb.admin.api.application.genre.update;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class UpdateGenreUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultUpdateGenreUseCase updateGenreUseCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway, genreGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
    //given
    final var aGenre = Genre.newGenre("act", true);

    final var expectedId = aGenre.getId();
    final var expectedName = "action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();
    final var firstCreatedAt = aGenre.getCreatedAt();
    final var firstUpdatedAt = aGenre.getUpdatedAt();

    final var aCommand = UpdateGenreCommand.with(
        expectedId,
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findByID(any()))
        .thenReturn(Optional.of(aGenre));

    when(genreGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    //when
    final var actualOutput = updateGenreUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, Mockito.times(1)).findByID(eq(expectedId));

    Mockito.verify(genreGateway, Mockito.times(1)).update(argThat(aUpdatedGenre ->
        Objects.equals(expectedName, aUpdatedGenre.getName()) &&
            Objects.equals(expectedId, aUpdatedGenre.getId()) &&
            Objects.equals(expectedIsActive, aUpdatedGenre.isActive()) &&
            Objects.equals(expectedCategories, aUpdatedGenre.getCategories()) &&
            Objects.equals(firstCreatedAt, aUpdatedGenre.getCreatedAt()) &&
            firstUpdatedAt.isBefore(aUpdatedGenre.getUpdatedAt()) &&
            Objects.isNull(aUpdatedGenre.getDeletedAt())
    ));
  }

  @Test
  public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
    //given
    final var aGenre = Genre.newGenre("act", true);

    final var expectedId = aGenre.getId();
    final var expectedName = "action";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();
    final var firstCreatedAt = aGenre.getCreatedAt();
    final var firstUpdatedAt = aGenre.getUpdatedAt();

    final var aCommand = UpdateGenreCommand.with(
        expectedId,
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findByID(any()))
        .thenReturn(Optional.of(aGenre));

    when(genreGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    Assertions.assertNull(aGenre.getDeletedAt());
    Assertions.assertTrue(aGenre.isActive());

    //when
    final var actualOutput = updateGenreUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, Mockito.times(1)).findByID(eq(expectedId));

    Mockito.verify(genreGateway, Mockito.times(1)).update(argThat(aUpdatedGenre ->
        Objects.equals(expectedName, aUpdatedGenre.getName()) &&
            Objects.equals(expectedId, aUpdatedGenre.getId()) &&
            Objects.equals(expectedIsActive, aUpdatedGenre.isActive()) &&
            Objects.equals(expectedCategories, aUpdatedGenre.getCategories()) &&
            Objects.equals(firstCreatedAt, aUpdatedGenre.getCreatedAt()) &&
            firstUpdatedAt.isBefore(aUpdatedGenre.getUpdatedAt()) &&
            Objects.nonNull(aUpdatedGenre.getDeletedAt())
    ));
  }

  @Test
  public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
    //given
    final var aGenre = Genre.newGenre("act", true);

    final var expectedId = aGenre.getId();
    final var expectedName = "action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of(
        CategoryID.from("123"),
        CategoryID.from("456")
    );

    final var firstCreatedAt = aGenre.getCreatedAt();
    final var firstUpdatedAt = aGenre.getUpdatedAt();

    final var aCommand = UpdateGenreCommand.with(
        expectedId,
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(categoryGateway.existsByIds(any()))
        .thenReturn(expectedCategories);

    when(genreGateway.findByID(any()))
        .thenReturn(Optional.of(aGenre));

    when(genreGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    //when
    final var actualOutput = updateGenreUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, Mockito.times(1)).findByID(eq(expectedId));

    Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(eq(expectedCategories));

    Mockito.verify(genreGateway, Mockito.times(1)).update(argThat(aUpdatedGenre ->
        Objects.equals(expectedName, aUpdatedGenre.getName()) &&
            Objects.equals(expectedId, aUpdatedGenre.getId()) &&
            Objects.equals(expectedIsActive, aUpdatedGenre.isActive()) &&
            Objects.equals(expectedCategories, aUpdatedGenre.getCategories()) &&
            Objects.equals(firstCreatedAt, aUpdatedGenre.getCreatedAt()) &&
            firstUpdatedAt.isBefore(aUpdatedGenre.getUpdatedAt()) &&
            Objects.isNull(aUpdatedGenre.getDeletedAt())
    ));
  }

  @Test
  public void givenAInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
    //given
    final var aGenre = Genre.newGenre("act", true);

    final var expectedId = aGenre.getId();
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;


    final var aCommand = UpdateGenreCommand.with(
        expectedId,
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findByID(any()))
        .thenReturn(Optional.of(aGenre));

    //when
    final var actualException = Assertions.assertThrows(NotificationException.class, () ->
        updateGenreUseCase.execute(aCommand)
    );

    //then
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    Mockito.verify(genreGateway, Mockito.times(1)).findByID(eq(expectedId));

    Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(eq(expectedCategories));

    Mockito.verify(genreGateway, Mockito.times(0)).update(any());
  }

  @Test
  public void givenAInvalidEmptyName_whenCallsUpdateGenre_shouldReturnNotificationException() {
    //given
    final var movies = CategoryID.from("1");
    final var documentaries = CategoryID.from("2");
    final var tvShows = CategoryID.from("3");

    final var aGenre = Genre.newGenre("act", true);

    final var expectedId = aGenre.getId();
    final var expectedName = "";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(movies, documentaries, tvShows);
    final var expectedErrorMessageOne = "Some categories could not be found: 2, 3";
    final var expectedErrorMessageTwo = "'name' should not be empty";
    final var expectedErrorMessageThree = "'name' must be between 1 and 255 characters";
    final var expectedErrorCount = 3;


    final var aCommand = UpdateGenreCommand.with(
        expectedId,
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findByID(any()))
        .thenReturn(Optional.of(aGenre));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(List.of(movies));

    //when
    final var actualException = Assertions.assertThrows(NotificationException.class, () ->
        updateGenreUseCase.execute(aCommand)
    );

    //then
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
    Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
    Assertions.assertEquals(expectedErrorMessageThree, actualException.getErrors().get(2).message());

    Mockito.verify(genreGateway, Mockito.times(1)).findByID(eq(expectedId));

    Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(eq(expectedCategories));

    Mockito.verify(genreGateway, Mockito.times(0)).update(any());
  }

  private List<String> asString(final List<CategoryID> categoryIDs) {
    return categoryIDs.stream()
        .map(CategoryID::getValue)
        .toList();
  }

}
