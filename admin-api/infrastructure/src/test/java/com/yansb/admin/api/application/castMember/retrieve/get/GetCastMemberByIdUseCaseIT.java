package com.yansb.admin.api.application.castMember.retrieve.get;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberJpaEntity;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class GetCastMemberByIdUseCaseIT {

  @Autowired
  private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

  @Autowired
  private CastMemberRepository castMemberRepository;

  @SpyBean
  private CastMemberGateway castMemberGateway;


  @Test
  public void givenACastMemberId_whenCallsGetMemberById_shouldReturnIt() {
    // given
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMembers.type();
    final var aMember = CastMember.newMember(expectedName, expectedType);

    final var expectedId = aMember.getId();

    this.castMemberRepository.save(CastMemberJpaEntity.from(aMember));

    Assertions.assertEquals(1, this.castMemberRepository.count());
    // when
    final var actualOutput = getCastMemberByIdUseCase.execute(expectedId.getValue());

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
    Assertions.assertEquals(expectedName, actualOutput.name());
    Assertions.assertEquals(expectedType, actualOutput.type());
    Assertions.assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
    Assertions.assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());

    verify(castMemberGateway).findByID(any());
  }

  @Test
  public void givenAInvalidId_whenCallsGetMemberByIdAndDoesNotExists_shouldThrowNotFoundException() {
    // given
    final var expectedId = CastMemberID.from("invalid-id");

    final var expectedErrorMessage = "CastMember with ID invalid-id was not found";

    // when
    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> getCastMemberByIdUseCase.execute(expectedId.getValue()));

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(actualException.getMessage(), expectedErrorMessage);

    verify(castMemberGateway).findByID(any());
  }
}

