package com.yansb.admin.api.infrastructure.castMember.models;

import com.yansb.admin.api.Fixture;
import com.yansb.admin.api.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
class UpdateCastMemberRequestTest {

  @Autowired
  private JacksonTester<UpdateCastMemberRequest> json;

  @Test
  public void testUnmarshall() throws IOException {
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();

    final var json = """
        {
          "name": "%s",
          "type": "%s"
        }
        """.formatted(
        expectedName,
        expectedType
    );

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("name", expectedName)
        .hasFieldOrPropertyWithValue("type", expectedType);

  }
}