package com.yansb.admin.api.infrastructure.genre.models;

import com.yansb.admin.api.JacksonTest;
import com.yansb.admin.api.infrastructure.category.models.CategoryResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@JacksonTest
public class GenreResponseTest {

  @Autowired
  private JacksonTester<GenreResponse> json;


  @Test
  public void testMarshall() throws IOException {
    final var expectedId = "123";
    final var expectedName = "Action";
    final var expectedCategories = List.of("123");
    final var expectedIsActive = true;
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();
    final var expectedDeletedAt = Instant.now();

    final var response = new GenreResponse(
        expectedId,
        expectedName,
        expectedCategories,
        expectedIsActive,
        expectedCreatedAt,
        expectedUpdatedAt,
        expectedDeletedAt
    );

    final var actualJson = this.json.write(response);

    Assertions.assertThat(actualJson)
        .hasJsonPathValue("$.id", expectedId)
        .hasJsonPathValue("$.name", expectedName)
        .hasJsonPathValue("$.categories_id", expectedCategories)
        .hasJsonPathValue("$.is_active", expectedIsActive)
        .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
        .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
        .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString());

  }

  @Test
  public void testUnmarshall() throws IOException {
    final var expectedId = "123";
    final var expectedName = "action";
    final var expectedCategoryId = "123";
    final var expectedIsActive = true;
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();
    final var expectedDeletedAt = Instant.now();

    final var json = """
        {
          "id": "%s",
          "name": "%s",
          "categories_id": ["%s"],
          "is_active": %s,
          "created_at": "%s",
          "updated_at": "%s",
          "deleted_at": "%s"
        }
        """.formatted(
              expectedId,
              expectedName,
              expectedCategoryId,
              expectedIsActive,
              expectedCreatedAt.toString(),
              expectedUpdatedAt.toString(),
              expectedDeletedAt.toString()
      );

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("id", expectedId)
        .hasFieldOrPropertyWithValue("name", expectedName)
        .hasFieldOrPropertyWithValue("categories", List.of(expectedCategoryId))
        .hasFieldOrPropertyWithValue("active", expectedIsActive)
        .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
        .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
        .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);

  }
}