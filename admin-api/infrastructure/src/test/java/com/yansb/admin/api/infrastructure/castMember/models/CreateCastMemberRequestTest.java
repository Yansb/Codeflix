package com.yansb.admin.api.infrastructure.castMember.models;

import com.yansb.admin.api.JacksonTest;
import com.yansb.admin.api.domain.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
class CreateCastMemberRequestTest {

  @Autowired
  private JacksonTester<CreateCastMemberRequest> json;

  @Test
  public void testUnmarshall() throws IOException {
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMembers.type();

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