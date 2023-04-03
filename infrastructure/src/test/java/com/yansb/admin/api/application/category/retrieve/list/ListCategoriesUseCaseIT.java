package com.yansb.admin.api.application.category.retrieve.list;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

@IntegrationTest
public class ListCategoriesUseCaseIT {

  @Autowired
  private ListCategoriesUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @BeforeEach
  void mockUp() {
    final var categories = Stream.of(
        Category.newCategory("Movies", null, true),
        Category.newCategory("Netflix originals", null, true),
        Category.newCategory("TV shows", null, true),
        Category.newCategory("Amazon originals", "Amazon authored shows", true),
        Category.newCategory("Sports", null, true),
        Category.newCategory("NFL", "NFL games", true),
        Category.newCategory("Kids", null, true)
    ).map(CategoryJpaEntity::from).toList();

    categoryRepository.saveAllAndFlush(categories);
  }

  @Test
  public void givenAValidTerm_whenTermDoesntMatchPrePersisted_shouldReturnEmptyPage() {
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerm = "non-existent";
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    final var expectedItemsCount = 0;
    final var expectedTotal = 0;

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerm, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
  }

  @ParameterizedTest
  @CsvSource({
      "mov,0,10,1,1,Movies",
      "net,0,10,1,1,Netflix originals",
      "ZON,0,10,1,1,Amazon originals",
      "games,0,10,1,1,NFL",
      "TV,0,10,1,1,TV shows",
  })
  public void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
      final String expectedTerm,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedCategoryName
  ){
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerm, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
  }

  @ParameterizedTest
  @CsvSource({
      "name,asc,0,10,7,7,Amazon originals",
      "name,desc,0,10,7,7,TV shows",
      "createdAt,asc,0,10,7,7,Movies",
      "createdAt,desc,0,10,7,7,Kids",
  })
  public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedCategoryName
  ){
    final var expectedTerm = "";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerm, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);
    System.out.println(actualResult.items());

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
  }

  @ParameterizedTest
  @CsvSource({
      "0,2,2,7,Amazon originals;Kids",
      "1,2,2,7,Movies;NFL",
      "2,2,2,7,Netflix originals, Sports",
      "3,2,1,7,TV shows"
  })
  public void givenAValidPage_whenCallsListCategories_thenShouldReturnCategoriesPaginated(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedCategoriesName
  ){
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    final var expectedTerm = "";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerm, expectedSort, expectedDirection);

    final var actualResult = useCase.execute(aQuery);
    System.out.println(actualResult.items());

    Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());

    int index = 0;
    for (String expectedName : expectedCategoriesName.split(";")){
      final String actualName = actualResult.items().get(index).name();
      Assertions.assertEquals(expectedName, actualName);
      index++;
    }

  }
}
