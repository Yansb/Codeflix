package com.yansb.admin.api.application.genre.retrieve.get;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultGetGenreByIdUseCase getGenreByIdUseCase;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of();
  }

  @Test
  public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
    // given
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        CategoryID.from("1"),
        CategoryID.from("2")
    );

    final var aGenre = Genre
        .newGenre(expectedName, expectedIsActive)
        .addCategories(expectedCategories);

    final var expectedId = aGenre.getId();

    Mockito.when(genreGateway.findByID(Mockito.any()))
        .thenReturn(Optional.of(aGenre));

    // when
    final var actualGenre = getGenreByIdUseCase.execute(expectedId.getValue());

    // then
    Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
    Assertions.assertEquals(expectedName, actualGenre.name());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

    Mockito.verify(genreGateway, Mockito.times(1)).findByID(expectedId);
  }

  @Test
  public void givenAValidId_whenCallsGetGenreAndGenreDoesNotExists_shouldReturnNotFound() {
    // given
    final var expectedErrorMessage = "Genre with ID 123 was not found";
    final var anId = GenreID.from("123");

    Mockito.when(genreGateway.findByID(Mockito.any()))
        .thenReturn(Optional.empty());

    // when
    final var actualException = Assertions.assertThrows(NotFoundException.class,
        () -> getGenreByIdUseCase.execute(anId.getValue())
    );

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }

  private List<String> asString(List<CategoryID> ids) {
    return ids.stream()
        .map(CategoryID::getValue)
        .toList();
  }
}
