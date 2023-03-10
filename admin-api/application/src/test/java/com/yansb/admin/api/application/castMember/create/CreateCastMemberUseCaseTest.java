package com.yansb.admin.api.application.castMember.create;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberType;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CreateCastMemberUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultCreateCastMemberUseCase createCastMemberUseCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
    //given
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMembers.type();

    final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

    when(castMemberGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    //when
    final var actualOutput = createCastMemberUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    verify(castMemberGateway).create(argThat(aMember ->
        Objects.nonNull(aMember.getId())
            && Objects.equals(expectedName, aMember.getName())
            && Objects.equals(expectedType, aMember.getType())
            && Objects.nonNull(aMember.getCreatedAt())
            && Objects.nonNull(aMember.getUpdatedAt())
    ));
  }

  @Test
  public void givenAInvalidName_whenCreateCastMember_shouldThrowANotificationException() {
    //given
    final String expectedName = null;
    final var expectedType = Fixture.CastMembers.type();

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

    //when
    final var actualException = Assertions.assertThrows(NotificationException.class, () ->
        createCastMemberUseCase.execute(aCommand));

    //then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(castMemberGateway, times(0)).create(any());
  }

  @Test
  public void givenAInvalidType_whenCreateCastMember_shouldThrowANotificationException() {
    //given
    final var expectedName = Fixture.name();
    final CastMemberType expectedType = null;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'type' should not be null";

    final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

    //when
    final var actualException = Assertions.assertThrows(NotificationException.class, () ->
        createCastMemberUseCase.execute(aCommand));

    //then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(castMemberGateway, times(0)).create(any());
  }
}
