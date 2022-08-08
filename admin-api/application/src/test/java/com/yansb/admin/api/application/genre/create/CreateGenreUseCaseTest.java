package com.yansb.admin.api.application.genre.create;

import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {

  @InjectMocks
  private DefaultCreateGenreUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @Test
  public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId(){
    // given
    final var expectName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    when(genreGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then

    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
        Objects.equals(expectName, aGenre.getName()) &&
        Objects.equals(expectedIsActive, aGenre.isActive()) &&
        Objects.equals(expectedCategories, aGenre.getCategories()) &&
        Objects.nonNull(aGenre.getId()) &&
        Objects.nonNull(aGenre.getCreatedAt()) &&
        Objects.nonNull(aGenre.getUpdatedAt()) &&
        Objects.isNull(aGenre.getDeletedAt())
    ));
  }

  @Test
  public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId(){
    // given
    final var expectName = "Action";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    when(genreGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then

    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
        Objects.equals(expectName, aGenre.getName()) &&
        Objects.equals(expectedIsActive, aGenre.isActive()) &&
        Objects.equals(expectedCategories, aGenre.getCategories()) &&
        Objects.nonNull(aGenre.getId()) &&
        Objects.nonNull(aGenre.getCreatedAt()) &&
        Objects.nonNull(aGenre.getUpdatedAt()) &&
        Objects.nonNull(aGenre.getDeletedAt())
    ));
  }

  @Test
  public void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreID(){
    // given
    final var expectName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        CategoryID.from("1"),
        CategoryID.from("2")
    );

    final var aCommand = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(expectedCategories);

    when(genreGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(categoryGateway, times(1)).existsByIds(argThat(aCategoryIds ->
        Objects.equals(expectedCategories, aCategoryIds)
    ));

    Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
        Objects.equals(expectName, aGenre.getName()) &&
            Objects.equals(expectedIsActive, aGenre.isActive()) &&
            Objects.equals(expectedCategories, aGenre.getCategories()) &&
            Objects.nonNull(aGenre.getId()) &&
            Objects.nonNull(aGenre.getCreatedAt()) &&
            Objects.nonNull(aGenre.getUpdatedAt()) &&
            Objects.isNull(aGenre.getDeletedAt())
    ));
  }

  @Test
  public void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException(){
    // given
    final var expectName = "";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorMessage = "'name' should not be empty";
    final var expectedErrorMessage2 = "'name' must be between 1 and 255 characters";
    final var expectedErrorCount = 2;

    final var aCommand = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class,
        () -> useCase.execute(aCommand)
    );

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    Mockito.verify(categoryGateway, times(0)).existsByIds(any());
    Mockito.verify(genreGateway, times(0)).create(any());
  }

  @Test
  public void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException(){
    // given
    final String expectName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand = CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class,
        () -> useCase.execute(aCommand)
    );

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    Mockito.verify(categoryGateway, times(0)).existsByIds(any());
    Mockito.verify(genreGateway, times(0)).create(any());
  }

  @Test
  public void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException(){
    // given
    final var movies = CategoryID.from("1");
    final var shows = CategoryID.from("2");
    final var documentaries = CategoryID.from("3");

    final String expectName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        movies,
        shows,
        documentaries
    );

    final var expectedErrorMessage = "Some categories could not be found: 2, 3";
    final var expectedErrorCount = 1;

    when(categoryGateway.existsByIds(any()))
        .thenReturn(List.of(movies));

    final var aCommand =
        CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class,
        () -> useCase.execute(aCommand)
    );

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());

    Mockito.verify(categoryGateway, times(1)).existsByIds(any());
    Mockito.verify(genreGateway, times(0)).create(any());
  }

  private List<String> asString(List<CategoryID> expectedCategories) {
    return expectedCategories.stream()
        .map(CategoryID::getValue)
        .toList();
  }

}
