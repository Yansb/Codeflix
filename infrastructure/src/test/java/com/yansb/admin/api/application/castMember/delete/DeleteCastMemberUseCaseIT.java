package com.yansb.admin.api.application.castMember.delete;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberJpaEntity;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class DeleteCastMemberUseCaseIT {

  @Autowired
  private DefaultDeleteCastMemberUseCase useCase;

  @Autowired
  private CastMemberRepository castMemberRepository;

  @SpyBean
  private CastMemberGateway castMemberGateway;

  @Test
  public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
    // given
    final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
    final var aMember2 = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
    final var expectedId = aMember.getId();

    this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
    this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember2));
    Assertions.assertEquals(2, this.castMemberRepository.count());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteByID(eq(expectedId));
    Assertions.assertEquals(1, this.castMemberRepository.count());
    Assertions.assertFalse(this.castMemberRepository.existsById(expectedId.getValue()));
    Assertions.assertTrue(this.castMemberRepository.existsById(aMember2.getId().getValue()));
  }

  @Test
  public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
    // given
    final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
    final var expectedId = aMember.getId();

    this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    doThrow(new IllegalStateException("Gateway error"))
        .when(castMemberGateway).deleteByID(any());

    Assertions.assertEquals(1, this.castMemberRepository.count());

    // when
    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteByID(eq(expectedId));
    Assertions.assertEquals(1, this.castMemberRepository.count());

  }

  @Test
  public void givenAInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
    // given
    final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
    final var expectedId = CastMemberID.from("invalid-id");

    this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
    Assertions.assertEquals(1, this.castMemberRepository.count());


    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteByID(eq(expectedId));
    Assertions.assertEquals(1, this.castMemberRepository.count());

  }
}
