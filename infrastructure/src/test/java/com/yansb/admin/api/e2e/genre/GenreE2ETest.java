package com.yansb.admin.api.e2e.genre;

import com.yansb.admin.api.ApiTest;
import com.yansb.admin.api.E2ETest;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.e2e.MockDsl;
import com.yansb.admin.api.infrastructure.genre.models.UpdateGenreRequest;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");
    @Autowired
    private MockMvc mvc;
    @Autowired
    private GenreRepository genreRepository;

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
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategoriesUsingValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("movies", null, true);
        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }


    @Test
    public void asACatalogAdminIShouldBeAbleThruNavigateToAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Drama", true, List.of());
        givenAGenre("Sports", true, List.of());

        listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Action")));

        listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Drama")));

        listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(2)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Sports")));

        listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(0)));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Drama", true, List.of());
        givenAGenre("Sports", true, List.of());

        listGenres(0, 1, "dra")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(1)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Drama")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        givenAGenre("Action", true, List.of());
        givenAGenre("Drama", true, List.of());
        givenAGenre("Sports", true, List.of());

        listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", Matchers.equalTo(0)))
                .andExpect(jsonPath("$.total", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.per_page", Matchers.equalTo(3)))
                .andExpect(jsonPath("$.items", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", Matchers.equalTo("Sports")))
                .andExpect(jsonPath("$.items[1].name", Matchers.equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", Matchers.equalTo("Action")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("movies", null, true);

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var aRequest = get("/genres/123")
                .with(ApiTest.ADMIN_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.equalTo("Genre with ID 123 was not found")));
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());


        final var movies = givenACategory("movies", null, true);

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre("act", expectedIsActive, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories, CategoryID::getValue), expectedIsActive);

        updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToInactivateAGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("movies", null, true);

        final var expectedName = "action";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, true, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories, CategoryID::getValue), expectedIsActive);

        updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNotNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToActivateAGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("movies", null, true);

        final var expectedName = "action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies);

        final var actualId = givenAGenre(expectedName, false, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(expectedName, mapTo(expectedCategories, CategoryID::getValue), expectedIsActive);

        updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(expectedCategories.size() == actualGenre.categories().size()
                && mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var movies = givenACategory("movies", null, true);


        final var actualId = givenAGenre("Action", true, List.of(movies));

        Assertions.assertEquals(1, genreRepository.count());

        deleteAGenre(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
        Assertions.assertEquals(0, genreRepository.count());
    }
}
