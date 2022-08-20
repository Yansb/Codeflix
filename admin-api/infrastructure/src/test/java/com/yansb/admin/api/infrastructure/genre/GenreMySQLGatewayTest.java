package com.yansb.admin.api.infrastructure.genre;

import com.yansb.admin.api.MySQLGatewayTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.infrastructure.category.CategoryMySQLGateway;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreJpaEntity;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

  @Autowired
  private CategoryMySQLGateway categoryMySQLGateway;

  @Autowired
  private GenreMySQLGateway genreMySQLGateway;

  @Autowired
  private GenreRepository genreRepository;

  @NotNull
  private static List<CategoryID> sorted(List<CategoryID> expectedCategories) {
    return expectedCategories.stream().sorted(
        Comparator.comparing(CategoryID::getValue)
    ).toList();
  }

  @Test
  public void testDependenciesInjected() {
    Assertions.assertNotNull(categoryMySQLGateway);
    Assertions.assertNotNull(genreRepository);
    Assertions.assertNotNull(genreMySQLGateway);
  }

  @Test
  public void givenAValidGenre_whenCallsCreateGenre_shouldReturnANewGenre() {
    final var movies = categoryMySQLGateway.create(Category.newCategory("Movies", null, true));

    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(movies.getId());

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    final var expectedId = aGenre.getId().getValue();
    aGenre.addCategories(expectedCategories);

    Assertions.assertEquals(0, genreRepository.count());

    final var actualGenre = genreMySQLGateway.create(aGenre);

    Assertions.assertEquals(1, genreRepository.count());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualGenre.getId().getValue(), expectedId);
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldReturnANewGenre() {

    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    final var expectedId = aGenre.getId().getValue();

    Assertions.assertEquals(0, genreRepository.count());

    final var actualGenre = genreMySQLGateway.create(aGenre);

    Assertions.assertEquals(1, genreRepository.count());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualGenre.getId().getValue(), expectedId);
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldReturnPersistGenre() {
    final var movies = categoryMySQLGateway.create(Category.newCategory("Movies", null, true));
    final var shows = categoryMySQLGateway.create(Category.newCategory("Shows", null, true));

    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(movies.getId(), shows.getId());

    final var aGenre = Genre.newGenre("Ac", expectedIsActive);
    final var expectedId = aGenre.getId().getValue();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));


    Assertions.assertEquals(1, genreRepository.count());
    Assertions.assertEquals(aGenre.getName(), "Ac");
    Assertions.assertEquals(aGenre.getCategories().size(), 0);


    final var actualGenre = genreMySQLGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
    Assertions.assertEquals(actualGenre.getId().getValue(), expectedId);
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldReturnPersistGenre() {
    final var movies = categoryMySQLGateway.create(Category.newCategory("Movies", null, true));
    final var shows = categoryMySQLGateway.create(Category.newCategory("Shows", null, true));

    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre("Ac", expectedIsActive);
    aGenre.addCategories(List.of(movies.getId(), shows.getId()));

    final var expectedId = aGenre.getId().getValue();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));


    Assertions.assertEquals(1, genreRepository.count());
    Assertions.assertEquals(aGenre.getName(), "Ac");
    Assertions.assertEquals(aGenre.getCategories().size(), 2);


    final var actualGenre = genreMySQLGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualGenre.getId().getValue(), expectedId);
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreInactive_whenCallsUpdateActivate_shouldReturnPersistGenre() {
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, false);

    final var expectedId = aGenre.getId().getValue();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));


    Assertions.assertEquals(1, genreRepository.count());
    Assertions.assertFalse(aGenre.isActive());
    Assertions.assertNotNull(aGenre.getDeletedAt());

    final var actualGenre = genreMySQLGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualGenre.getId().getValue(), expectedId);
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertEquals(actualGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreActive_whenCallsUpdateInactivate_shouldReturnPersistGenre() {
    final var expectedName = "Action";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, true);

    final var expectedId = aGenre.getId().getValue();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));


    Assertions.assertEquals(1, genreRepository.count());
    Assertions.assertTrue(aGenre.isActive());
    Assertions.assertNull(aGenre.getDeletedAt());

    final var actualGenre = genreMySQLGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualGenre.getId().getValue(), expectedId);
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNotNull(persistedGenre.getDeletedAt());
  }
}
