package com.yansb.admin.api.application.genre.delete;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteGenreUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultDeleteGenreUseCase deleteGenreUseCase;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(genreGateway);
  }

  @Test
  public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
    //given
    final var aGenre = Genre.newGenre("Action", true);
    final var expectedId = aGenre.getId();

    Mockito.doNothing()
        .when(genreGateway).deleteByID(Mockito.any());
    //when
    Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

    //then
    Mockito.verify(genreGateway, Mockito.times(1)).deleteByID(expectedId);
  }

  @Test
  public void givenAInvalidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
    //given
    final var expectedId = GenreID.from("123");

    Mockito.doNothing()
        .when(genreGateway).deleteByID(Mockito.any());
    //when
    Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

    //then
    Mockito.verify(genreGateway, Mockito.times(1)).deleteByID(expectedId);
  }

  @Test
  public void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveAnException() {
    //given
    final var aGenre = Genre.newGenre("Action", true);
    final var expectedId = aGenre.getId();

    Mockito.doThrow(new IllegalStateException("Gateway error"))
        .when(genreGateway).deleteByID(Mockito.any());
    //when
    Assertions.assertThrows(IllegalStateException.class,
        () -> deleteGenreUseCase.execute(expectedId.getValue()));

    //then
    Mockito.verify(genreGateway, Mockito.times(1)).deleteByID(expectedId);
  }
}
