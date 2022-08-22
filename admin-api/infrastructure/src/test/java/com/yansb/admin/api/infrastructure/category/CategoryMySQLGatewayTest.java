package com.yansb.admin.api.infrastructure.category;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.MySQLGatewayTest;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

  @Autowired
  private CategoryMySQLGateway categoryGateway;

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;

    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    Assertions.assertEquals(0, categoryRepository.count());


    final var actualCategory = categoryGateway.create(aCategory);

    Assertions.assertEquals(1, categoryRepository.count());

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
    Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
    Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
    Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
    Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());

    final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

    Assertions.assertEquals(expectedName, actualEntity.getName());
    Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
    Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
    Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
    Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
    Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
  }

  @Test
  public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;

    final var aCategory = Category.newCategory("Mov", null, expectedIsActive);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    Assertions.assertEquals(1, categoryRepository.count());
    final var beforeUpdateCategory = categoryRepository.findById(aCategory.getId().getValue()).get();
    Assertions.assertEquals("Mov", beforeUpdateCategory.getName());


    final var aUpdatedCategory = aCategory.clone()
        .update(expectedName, expectedDescription, expectedIsActive);

    final var actualCategory = categoryGateway.update(aUpdatedCategory);

    Assertions.assertEquals(1, categoryRepository.count());

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
    Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
    Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
    Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
    Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());

    final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

    Assertions.assertEquals(expectedName, actualEntity.getName());
    Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
    Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
    Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
    Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
    Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
  }

  @Test
  public void givenAValidCategoryId_whenCallsDelete_shouldReturnDeleteCategory(){
    final var aCategory = Category.newCategory("Movies", null, false);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    Assertions.assertEquals(1, categoryRepository.count());

    categoryGateway.deleteById(aCategory.getId());
    Assertions.assertEquals(0, categoryRepository.count());

  }

  @Test
  public void givenAInvalidCategoryId_whenCallsDelete_shouldReturnDeleteCategory(){
    Assertions.assertEquals(0, categoryRepository.count());

    categoryGateway.deleteById(CategoryID.from("123"));

    Assertions.assertEquals(0, categoryRepository.count());

  }


  @Test
  public void givenAValidCategoryID_whenCallsFindByID_shouldReturnCategory(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;

    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    Assertions.assertEquals(1, categoryRepository.count());

    final var actualCategory = categoryGateway.findById(aCategory.getId()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
    Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
    Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
    Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
    Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
  }

  @Test
  public void givenAValidCategoryIDNotPersisted_whenCallsFindByID_shouldReturnEmpty(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;

    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    Assertions.assertEquals(0, categoryRepository.count());

    final var actualCategory = categoryGateway.findById(aCategory.getId());

    Assertions.assertTrue(actualCategory.isEmpty());
  }

  @Test
  public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated(){
    final var expectedPage = 0;
    final var perPage = 1;
    final var total = 3;

    final var movies = Category.newCategory("Movies", null, true);
    final var shows = Category.newCategory("Shows", null, true);
    final var documentaries = Category.newCategory("Documentaries", null, true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(movies),
        CategoryJpaEntity.from(shows),
        CategoryJpaEntity.from(documentaries)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    final var query = new SearchQuery(0, 1, "", "name", "asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(perPage, actualResult.perPage());
    Assertions.assertEquals(total, actualResult.total());
    Assertions.assertEquals(perPage, actualResult.items().size());
    Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
  }

  @Test
  public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage(){
    final var expectedPage = 0;
    final var perPage = 1;
    final var total = 0;

    Assertions.assertEquals(0, categoryRepository.count());

    final var query = new SearchQuery(0, 1, "", "name", "asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(perPage, actualResult.perPage());
    Assertions.assertEquals(total, actualResult.total());
    Assertions.assertEquals(0, actualResult.items().size());
  }

  @Test
  public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated(){
    final var expectedPage = 0;
    final var perPage = 1;
    final var total = 3;

    final var movies = Category.newCategory("Movies", null, true);
    final var shows = Category.newCategory("Shows", null, true);
    final var documentaries = Category.newCategory("Documentaries", null, true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(movies),
        CategoryJpaEntity.from(shows),
        CategoryJpaEntity.from(documentaries)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    var query = new SearchQuery(0, 1, "", "name", "asc");
    var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(perPage, actualResult.perPage());
    Assertions.assertEquals(total, actualResult.total());
    Assertions.assertEquals(perPage, actualResult.items().size());
    Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());

    query = new SearchQuery(1, 1, "", "name", "asc");
    actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(1, actualResult.currentPage());
    Assertions.assertEquals(perPage, actualResult.perPage());
    Assertions.assertEquals(total, actualResult.total());
    Assertions.assertEquals(perPage, actualResult.items().size());
    Assertions.assertEquals("Movies", actualResult.items().get(0).getName());

    query = new SearchQuery(2, 1, "", "name", "asc");
    actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(2, actualResult.currentPage());
    Assertions.assertEquals(perPage, actualResult.perPage());
    Assertions.assertEquals(total, actualResult.total());
    Assertions.assertEquals(perPage, actualResult.items().size());
    Assertions.assertEquals("Shows", actualResult.items().get(0).getName());
  }

  @Test
  public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated(){
    final var expectedPage = 0;
    final var perPage = 1;
    final var total = 1;

    final var movies = Category.newCategory("Movies", null, true);
    final var shows = Category.newCategory("Shows", null, true);
    final var documentaries = Category.newCategory("Documentaries", null, true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(movies),
        CategoryJpaEntity.from(shows),
        CategoryJpaEntity.from(documentaries)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    final var query = new SearchQuery(0, 1, "doc", "name", "asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(perPage, actualResult.total());
    Assertions.assertEquals(total, actualResult.total());
    Assertions.assertEquals(perPage, actualResult.items().size());
    Assertions.assertEquals(documentaries.getId(), actualResult.items().get(0).getId());
  }

  @Test
  public void givenPrePersistedCategoriesAndMostWatchedAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated(){
    final var expectedPage = 0;
    final var perPage = 1;
    final var total = 1;

    final var movies = Category.newCategory("Movies", "Most watched Category", true);
    final var shows = Category.newCategory("Shows", "a category", true);
    final var documentaries = Category.newCategory("Documentaries", "Lest watched Category", true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(movies),
        CategoryJpaEntity.from(shows),
        CategoryJpaEntity.from(documentaries)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    final var query = new SearchQuery(0, 1, "MOST WATCHED", "name", "asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(perPage, actualResult.total());
    Assertions.assertEquals(total, actualResult.total());
    Assertions.assertEquals(perPage, actualResult.items().size());
    Assertions.assertEquals(movies.getId(), actualResult.items().get(0).getId());
  }
  @Test
  public void givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds(){
    final var movies = Category.newCategory("Movies", "Most watched Category", true);
    final var shows = Category.newCategory("Shows", "a category", true);
    final var documentaries = Category.newCategory("Documentaries", "Lest watched Category", true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(movies),
        CategoryJpaEntity.from(shows),
        CategoryJpaEntity.from(documentaries)
    ));

    Assertions.assertEquals(3, categoryRepository.count());
    final var ids = List.of(movies.getId(), shows.getId(), CategoryID.from("123"));
    final var expectedIds = List.of(movies.getId(), shows.getId());


    final var actualResult = categoryGateway.existsByIds(ids);

    Assertions.assertTrue(expectedIds.size() == actualResult.size()
    && expectedIds.containsAll(actualResult)
        && actualResult.containsAll(expectedIds));
  }
}

