package com.yansb.admin.api.infrastructure.castMember.models;

import com.yansb.admin.api.JacksonTest;
import com.yansb.admin.api.domain.Fixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
class CastMemberListResponseTest {

  @Autowired
  private JacksonTester<CastMemberListResponse> json;

  @Test
  public void testMarshall() throws IOException {
    final var expectedId = Fixture.id();
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMembers.type();
    final var expectedCreatedAt = Instant.now().toString();

    final var response = new CastMemberListResponse(
        expectedId,
        expectedName,
        expectedType,
        expectedCreatedAt
    );

    final var actualJson = this.json.write(response);

    Assertions.assertThat(actualJson)
        .hasJsonPathValue("$.id", expectedId)
        .hasJsonPathValue("$.name", expectedName)
        .hasJsonPathValue("$.type", expectedType)
        .hasJsonPathValue("$.created_at", expectedCreatedAt);

  }
}