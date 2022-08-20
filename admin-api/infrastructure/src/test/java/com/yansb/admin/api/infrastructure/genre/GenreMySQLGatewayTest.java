package com.yansb.admin.api.infrastructure.genre;

import com.yansb.admin.api.MySQLGatewayTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.category.CategoryMySQLGateway;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreJpaEntity;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

  @Test
  public void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre(){
    // given
    final var aGenre = Genre.newGenre("Action", true);

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals(1, genreRepository.count());
    //when
    genreMySQLGateway.deleteByID(aGenre.getId());

    //then
    Assertions.assertEquals(0, genreRepository.count());
  }
  @Test
  public void givenAInvalidId_whenCallsDeleteById_shouldDeleteGenre(){
    // given
    Assertions.assertEquals(0, genreRepository.count());

    //when
    genreMySQLGateway.deleteByID(GenreID.from("123"));

    //then
    Assertions.assertEquals(0, genreRepository.count());
  }
  @Test
  public void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre(){
    //given
    final var movies = categoryMySQLGateway.create(Category.newCategory("Movies", null, true));
    final var shows = categoryMySQLGateway.create(Category.newCategory("Shows", null, true));

    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(movies.getId(), shows.getId());

    final var aGenre = Genre.newGenre("Action", true);
    aGenre.addCategories(expectedCategories);

    final var expectedId = aGenre.getId();

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals(1, genreRepository.count());

    //when
    final var actualGenre = genreMySQLGateway.findByID(expectedId).get();

    //then

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
    Assertions.assertEquals(actualGenre.getId(), expectedId);
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAInvalidGenreId_whenCallsFindById_shouldReturnGenre(){
    //given
    final var expectedId = GenreID.from("123");

    Assertions.assertEquals(0, genreRepository.count());

    //when
    final var actualGenre = genreMySQLGateway.findByID(expectedId);

    //then
    Assertions.assertTrue(actualGenre.isEmpty());
  }
  @Test
  public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList(){
    //given
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 0;
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
          new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    //when
    final var actualPage = genreMySQLGateway.findAll(aQuery);

    //then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());
  }
  @ParameterizedTest
  @CsvSource({
    "ac,0,10,1,1,Action",
    "dr,0,10,1,1,Drama",
    "com,0,10,1,1,Comedy",
    "sci,0,10,1,1,Fictional Science",
    "hor,0,10,1,1,Horror",
  })
  public void givenAValidTerm_whenCallsFindAll_shouldReturnGenresListFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
  ){
    //given
    mockGenres();
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
          new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    //when
    final var actualPage = genreMySQLGateway.findAll(aQuery);

    //then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
  }
  @ParameterizedTest
  @CsvSource({
    "name,asc,0,10,5,5,Action",
    "name,desc,0,10,5,5,Horror",
    "createdAt,asc,0,10,5,5,Comedy",
    "createdAt,desc,0,10,5,5,Fictional Science",
  })
  public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnGenresListFiltered(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
  ){
    mockGenres();
    final var expectedTerms = "";
    //given
    final var aQuery =
          new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    //when
    final var actualPage = genreMySQLGateway.findAll(aQuery);

    //then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
  }
  @ParameterizedTest
  @CsvSource({
    "0,2,2,5,Action;Comedy",
    "1,2,2,5,Drama;Fictional Science",
    "2,2,1,5,Horror",
  })
  public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnGenresListFiltered(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenres
  ){
    mockGenres();
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    //given
    final var aQuery =
          new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    //when
    final var actualPage = genreMySQLGateway.findAll(aQuery);

    //then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

    int index = 0;
    for (final var expectedName : expectedGenres.split(";")) {
      final var actualName =actualPage.items().get(index).getName();
      Assertions.assertEquals(expectedName, actualName);
      index++;
    }

  }

  private void mockGenres(){
    genreRepository.saveAllAndFlush(List.of(
        GenreJpaEntity.from(Genre.newGenre("Comedy", true)),
        GenreJpaEntity.from(Genre.newGenre("Action", true)),
        GenreJpaEntity.from(Genre.newGenre("Drama", true)),
        GenreJpaEntity.from(Genre.newGenre("Horror", true)),
        GenreJpaEntity.from(Genre.newGenre("Fictional Science", true))
    ));
  }

  @NotNull
  private static List<CategoryID> sorted(List<CategoryID> expectedCategories) {
    return expectedCategories.stream().sorted(
        Comparator.comparing(CategoryID::getValue)
    ).toList();
  }
}
