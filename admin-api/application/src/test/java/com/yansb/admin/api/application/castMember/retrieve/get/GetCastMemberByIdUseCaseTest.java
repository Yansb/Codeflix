package com.yansb.admin.api.application.castMember.retrieve.get;

import com.yansb.admin.api.application.Fixture;
import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetCastMemberByIdUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  public void givenACastMemberId_whenCallsGetMemberById_shouldReturnIt() {
    // given
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMembers.type();
    final var aMember = CastMember.newMember(expectedName, expectedType);

    final var expectedId = aMember.getId();

    when(castMemberGateway.findByID(any()))
        .thenReturn(Optional.of(aMember));
    // when

    final var actualOutput = getCastMemberByIdUseCase.execute(expectedId.getValue());

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
    Assertions.assertEquals(expectedName, actualOutput.name());
    Assertions.assertEquals(expectedType, actualOutput.type());
    Assertions.assertEquals(aMember.getCreatedAt(), actualOutput.createdAt());
    Assertions.assertEquals(aMember.getUpdatedAt(), actualOutput.updatedAt());

    verify(castMemberGateway).findByID(eq(expectedId));
  }

  @Test
  public void givenAInvalidId_whenCallsGetMemberByIdAndDoesNotExists_shouldThrowNotFoundException() {
    // given
    final var expectedId = CastMemberID.from("invalid-id");

    final var expectedErrorMessage = "CastMember with ID invalid-id was not found";

    when(castMemberGateway.findByID(any()))
        .thenReturn(Optional.empty());
    // when

    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> getCastMemberByIdUseCase.execute(expectedId.getValue()));

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(actualException.getMessage(), expectedErrorMessage);

    verify(castMemberGateway).findByID(eq(expectedId));
  }
}

