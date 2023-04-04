package com.yansb.admin.api.e2e.categories;

import com.yansb.admin.api.ApiTest;
import com.yansb.admin.api.E2ETest;
import com.yansb.admin.api.e2e.MockDsl;
import com.yansb.admin.api.infrastructure.category.models.UpdateCategoryRequest;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest implements MockDsl {

    @Container
    private static final MySQLContainer MYSQL_CONTAINER
            = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CategoryRepository categoryRepository;

    @DynamicPropertySource
    public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.printf("Mapped port %d%n", mappedPort);
        registry.add("mysql.port", () -> mappedPort);

    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveACategory(actualId);

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("movies", "Most watched category", true);
        givenACategory("shows", null, true);
        givenACategory("documentaries", "least watched category", true);

        listCategories(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("documentaries")));

        listCategories(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("movies")));

        listCategories(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("shows")));

        listCategories(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(0)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("movies", "Most watched category", true);
        givenACategory("shows", null, true);
        givenACategory("documentaries", "least watched category", true);

        listCategories(0, 1, "mov")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("movies")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescription() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("movies", "A", true);
        givenACategory("shows", "B", true);
        givenACategory("documentaries", "C", true);

        listCategories(0, 3, "", "description", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("documentaries")))
                .andExpect(jsonPath("$.items[1].name", Matchers.equalTo("shows")))
                .andExpect(jsonPath("$.items[2].name", Matchers.equalTo("movies")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var aRequest = get("/categories/123")
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Category with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());


        final var actualId = givenACategory("mov", null, true);

        final var expectedName = "movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        updateACategory(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateACategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = false;

        final var actualId = givenACategory(expectedName, expectedDescription, true);

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        updateACategory(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateACategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var actualId = givenACategory(expectedName, expectedDescription, false);

        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        updateACategory(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }


    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualId = givenACategory("Movies", null, true);

        deleteACategory(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.categoryRepository.existsById(actualId.getValue()));
    }

}
