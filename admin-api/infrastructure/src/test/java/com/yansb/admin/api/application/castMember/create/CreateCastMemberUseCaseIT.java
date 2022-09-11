package com.yansb.admin.api.application.castMember.create;

import com.yansb.admin.api.Fixture;
import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberType;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class CreateCastMemberUseCaseIT {

  @Autowired
  private CreateCastMemberUseCase createCastMemberUseCase;

  @Autowired
  private CastMemberRepository castMemberRepository;

  @SpyBean
  private CastMemberGateway castMemberGateway;


  @Test
  public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
    //given
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();

    final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

    //when
    final var actualOutput = createCastMemberUseCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualMember = this.castMemberRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertNotNull(actualMember.getCreatedAt());
    Assertions.assertNotNull(actualMember.getUpdatedAt());

    verify(castMemberGateway).create(any());
  }

  @Test
  public void givenAInvalidName_whenCreateCastMember_shouldThrowANotificationException() {
    //given
    final String expectedName = null;
    final var expectedType = Fixture.CastMember.type();

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
