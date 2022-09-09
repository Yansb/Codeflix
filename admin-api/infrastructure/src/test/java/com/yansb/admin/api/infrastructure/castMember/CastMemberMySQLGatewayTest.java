package com.yansb.admin.api.infrastructure.castMember;

import com.yansb.admin.api.Fixture;
import com.yansb.admin.api.MySQLGatewayTest;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CastMemberMySQLGatewayTest {
  @Autowired
  private CastMemberMySQLGateway castMemberMySQLGateway;

  @Autowired
  private CastMemberRepository castMemberRepository;

  @Test
  public void testDependencies() {
    Assertions.assertNotNull(castMemberMySQLGateway);
    Assertions.assertNotNull(castMemberRepository);
  }

  @Test
  public void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
    // given
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();

    final var aMember = CastMember.newMember(expectedName, expectedType);

    final var expectedId = aMember.getId();

    Assertions.assertEquals(0, castMemberRepository.count());

    // when
    final var actualMember = castMemberMySQLGateway.create(CastMember.with(aMember));

    // then
    Assertions.assertEquals(1, castMemberRepository.count());
    Assertions.assertEquals(expectedId, actualMember.getId());
    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertEquals(actualMember.getCreatedAt(), aMember.getCreatedAt());
    Assertions.assertEquals(actualMember.getUpdatedAt(), aMember.getUpdatedAt());

    final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedMember.getName());
    Assertions.assertEquals(expectedType, persistedMember.getType());
    Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
    Assertions.assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());
  }
}