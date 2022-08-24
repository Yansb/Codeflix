package com.yansb.admin.api.application.genre.update;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;

@IntegrationTest
public class UpdateGenreUseCaseIT {

  @Autowired
  private DefaultUpdateGenreUseCase updateGenreUseCase;

  @SpyBean
  private CategoryGateway categoryGateway;

  @SpyBean
  private GenreGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
    //given
    final var aGenre = genreGateway.create(Genre.newGenre("act", true));

    final var expectedId = aGenre.getId();
    final var expectedName = "action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand = UpdateGenreCommand.with(
        expectedId,
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    //when
    final var actualOutput = updateGenreUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    final var actualGenre = genreRepository.findById(aGenre.getId().getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertTrue(
        expectedCategories.size() == actualGenre.getCategoryIDs().size()
            && expectedCategories.containsAll(actualGenre.getCategoryIDs())
    );
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
    //given
    final var aGenre = genreGateway.create(Genre.newGenre("act", true));

    final var expectedId = aGenre.getId();
    final var expectedName = "action";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand = UpdateGenreCommand.with(
        expectedId,
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    Assertions.assertNull(aGenre.getDeletedAt());
    Assertions.assertTrue(aGenre.isActive());

    //when
    final var actualOutput = updateGenreUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    final var actualGenre = genreRepository.findById(aGenre.getId().getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertTrue(
            expectedCategories.size() == actualGenre.getCategoryIDs().size()
                    && expectedCategories.containsAll(actualGenre.getCategoryIDs())
    );
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
    //given
    final var action = categoryGateway.create(Category.newCategory("action", "", true));
    final var shows = categoryGateway.create(Category.newCategory("shows", "", true));

    final var aGenre = genreGateway.create(Genre.newGenre("act", true));

    final var expectedId = aGenre.getId();
    final var expectedName = "action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(action.getId(), shows.getId());

    final var aCommand = UpdateGenreCommand.with(
            expectedId,
            expectedName,
            expectedIsActive,
            asString(expectedCategories)
    );

    Assertions.assertNull(aGenre.getDeletedAt());
    Assertions.assertTrue(aGenre.isActive());

    //when
    final var actualOutput = updateGenreUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    final var actualGenre = genreRepository.findById(aGenre.getId().getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertTrue(
            expectedCategories.size() == actualGenre.getCategoryIDs().size()
                    && expectedCategories.containsAll(actualGenre.getCategoryIDs())
    );
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(actualGenre.getUpdatedAt().isAfter(aGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
    //given
    final var aGenre = genreGateway.create(Genre.newGenre("act", true));

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
    final var movies = categoryGateway.create(Category.newCategory("movies", "", true));
    final var documentaries = CategoryID.from("2");
    final var tvShows = CategoryID.from("3");

    final var aGenre = genreGateway.create(Genre.newGenre("act", true));

    final var expectedId = aGenre.getId();
    final var expectedName = "";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(movies.getId(), documentaries, tvShows);
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
