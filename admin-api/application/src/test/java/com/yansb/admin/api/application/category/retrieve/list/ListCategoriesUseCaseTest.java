package com.yansb.admin.api.application.category.retrieve.list;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

  @InjectMocks
  private DefaultListCategoriesUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @BeforeEach
  void cleanUp() {
    reset(categoryGateway);
  }

  @Test
  public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories(){
    final var categories = List.of(
        Category.newCategory("Movies", "Most watched category", true),
        Category.newCategory("Books", "Most read category", true)
        );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";


    final var aQuery = new SearchQuery(expectedPage, expectedPerPage,expectedTerms,expectedSort,expectedDirection);

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        categories.size(),
        categories
    );

    final var expectedItensCount = 2;
    final var expectedResult = expectedPagination.map(CategoryListOutput::from);

    when(categoryGateway.findAll(Mockito.eq(aQuery)))
        .thenReturn(expectedPagination);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItensCount, actualResult.items().size());
    Assertions.assertEquals(expectedResult, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(categories.size(), actualResult.total());
  }

  @Test
  public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyList(){
    final var categories = List.<Category>of();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var aQuery = new SearchQuery(expectedPage, expectedPerPage,expectedTerms,expectedSort,expectedDirection);

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        0,
        categories
    );

    final var expectedItensCount = 0;
    final var expectedResult = expectedPagination
        .map(CategoryListOutput::from);

    when(categoryGateway.findAll(Mockito.eq(aQuery)))
        .thenReturn(expectedPagination);

    final var actualResult = useCase.execute(aQuery);

    Assertions.assertEquals(expectedItensCount, actualResult.items().size());
    Assertions.assertEquals(expectedResult, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(0, actualResult.total());
  }

  @Test
  public void givenAValidQuery_whenGatewayThrowsException_thenShouldThrowException(){
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedErrorMessage= "Error";

    final var aQuery = new SearchQuery(expectedPage, expectedPerPage,expectedTerms,expectedSort,expectedDirection);

    when(categoryGateway.findAll(Mockito.eq(aQuery)))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var actualException = Assertions.assertThrows(
        IllegalStateException.class,
        () -> useCase.execute(aQuery)
    );

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

  }
}
