package com.yansb.admin.api.application.genre.delete;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


@IntegrationTest
public class DeleteGenreByIdUseCaseIT {

  @Autowired
  private DeleteGenreUseCase deleteGenreUseCase;

  @Autowired
  private GenreGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
    //given
    final var aGenre = genreGateway.create(Genre.newGenre("Action", true));

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(1, genreRepository.count());

    //when
    Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

    //then
    Assertions.assertEquals(0, genreRepository.count());
  }

  @Test
  public void givenAInvalidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
    //given
    genreGateway.create(Genre.newGenre("Drama", true));

    final var expectedId = GenreID.from("123");

    Assertions.assertEquals(1, genreRepository.count());

    //when
    Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId.getValue()));

    //then
    Assertions.assertEquals(1, genreRepository.count());
  }
}
