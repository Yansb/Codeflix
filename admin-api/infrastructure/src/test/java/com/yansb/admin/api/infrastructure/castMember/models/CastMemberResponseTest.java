package com.yansb.admin.api.infrastructure.castMember.models;

import com.yansb.admin.api.Fixture;
import com.yansb.admin.api.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
class CastMemberResponseTest {

  @Autowired
  private JacksonTester<CastMemberResponse> json;

  @Test
  public void testMarshall() throws IOException {
    final var expectedId = Fixture.id();
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();

    final var response = new CastMemberResponse(
        expectedId,
        expectedName,
        expectedType,
        expectedCreatedAt,
        expectedUpdatedAt
    );

    final var actualJson = this.json.write(response);

    Assertions.assertThat(actualJson)
        .hasJsonPathValue("$.id", expectedId)
        .hasJsonPathValue("$.name", expectedName)
        .hasJsonPathValue("$.type", expectedType)
        .hasJsonPathValue("$.created_at", expectedCreatedAt)
        .hasJsonPathValue("$.updated_at", expectedUpdatedAt);

  }
}