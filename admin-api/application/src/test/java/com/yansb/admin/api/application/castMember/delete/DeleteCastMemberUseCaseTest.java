package com.yansb.admin.api.application.castMember.delete;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultDeleteCastMemberUseCase useCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
    // given
    final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
    final var expectedId = aMember.getId();

    doNothing()
        .when(castMemberGateway).deleteByID(any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteByID(eq(expectedId));

  }

  @Test
  public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
    // given
    final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
    final var expectedId = aMember.getId();

    doThrow(new IllegalStateException("Gateway error"))
        .when(castMemberGateway).deleteByID(any());

    // when
    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteByID(eq(expectedId));

  }

  @Test
  public void givenAInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
    // given
    final var expectedId = CastMemberID.from("invalid-id");

    doNothing()
        .when(castMemberGateway).deleteByID(any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteByID(eq(expectedId));

  }
}
