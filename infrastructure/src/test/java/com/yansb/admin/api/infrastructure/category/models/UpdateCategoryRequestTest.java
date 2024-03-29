package com.yansb.admin.api.infrastructure.category.models;

import com.yansb.admin.api.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class UpdateCategoryRequestTest {

  @Autowired
  private JacksonTester<UpdateCategoryRequest> json;

  @Test
  public void testUnmarshall() throws IOException {
    final var expectedName = "movies";
    final var expectedDescription = "movies category";
    final var expectedIsActive = true;

    final var json = """
        {
          "name": "%s",
          "description": "%s",
          "is_active": %s
        }
        """.formatted(
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("name", expectedName)
        .hasFieldOrPropertyWithValue("description", expectedDescription)
        .hasFieldOrPropertyWithValue("active", expectedIsActive);

  }

}
