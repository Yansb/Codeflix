package com.yansb.admin.api.e2e.categories;

import com.yansb.admin.api.E2ETest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.infrastructure.category.models.CategoryResponse;
import com.yansb.admin.api.infrastructure.category.models.CreateCategoryRequest;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import com.yansb.admin.api.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CategoryRepository categoryRepository;

  @Container
  private static final MySQLContainer MYSQL_CONTAINER
      = new MySQLContainer("mysql:latest")
      .withPassword("123456")
      .withUsername("root")
      .withDatabaseName("adm_videos");

  @DynamicPropertySource
  public static void setDataSourceProperties(final DynamicPropertyRegistry registry){
    final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
    System.out.printf("Mapped port %d%n", mappedPort);
    registry.add("mysql.port", () -> mappedPort);

  }

  @Test
  public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

    Assertions.assertEquals(0, categoryRepository.count());

    final var expectedName = "movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;

    final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

    final var actualCategory = retrieveACategory(actualId.getValue());

    Assertions.assertEquals(expectedName, actualCategory.name());
    Assertions.assertEquals(expectedDescription, actualCategory.description());
    Assertions.assertEquals(expectedIsActive, actualCategory.active());
    Assertions.assertNotNull(actualCategory.createdAt());
    Assertions.assertNotNull(actualCategory.updatedAt());
    Assertions.assertNull(actualCategory.deletedAt());
  }

  private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
    final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);

    final var aRequest = post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(Json.writeValueAsString(aRequestBody));

    final var actualId = Objects.requireNonNull(this.mvc.perform(aRequest)
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse().getHeader("Location"))
        .replace("/categories/", "");

    return CategoryID.from(actualId);
  }

  private CategoryResponse retrieveACategory(final String anId) throws Exception {

    final var aRequest = get("/categories/" + anId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    final var json = this.mvc.perform(aRequest)
        .andExpect(status().isOk())
        .andReturn()
        .getResponse().getContentAsString();

    return Json.readValue(json, CategoryResponse.class);
  }
}
