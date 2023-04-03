package com.yansb.admin.api.application.castMember.update;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.castMember.CastMemberType;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberJpaEntity;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {
  @Autowired
  private DefaultUpdateCastMemberUseCase useCase;

  @Autowired
  private CastMemberRepository repository;

  @SpyBean
  private CastMemberGateway castMemberGateway;

  @Test
  public void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
    // given
    final var aMember = CastMember.newMember("John Doe", CastMemberType.DIRECTOR);

    repository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    final var expectedId = aMember.getId();
    final var expectedName = Fixture.name();
    final var expectedType = CastMemberType.ACTOR;

    final var aCommand = UpdateCastMemberCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedType
    );

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    final var actualPersistedMember = this.repository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, actualPersistedMember.getName());
    Assertions.assertEquals(expectedType, actualPersistedMember.getType());
    Assertions.assertEquals(aMember.getCreatedAt(), actualPersistedMember.getCreatedAt());
    Assertions.assertTrue(actualPersistedMember.getUpdatedAt().isAfter(aMember.getCreatedAt()));

    verify(castMemberGateway).findByID(any());
    verify(castMemberGateway).update(any());
  }

  @Test
  public void givenAInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
    // given
    final var aMember = CastMember.newMember("John Doe", CastMemberType.DIRECTOR);

    repository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    final var expectedId = aMember.getId();
    final String expectedName = null;
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";


    final var aCommand = UpdateCastMemberCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedType
    );
    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

    // then

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(actualException.getErrors().size(), expectedErrorCount);
    Assertions.assertEquals(actualException.getErrors().get(0).message(), expectedErrorMessage);

    verify(castMemberGateway).findByID(any());

    verify(castMemberGateway, times(0)).update(any());
  }

  @Test
  public void givenAInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
    // given
    final var aMember = CastMember.newMember("John Doe", CastMemberType.DIRECTOR);
    this.repository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    final var expectedId = aMember.getId();
    final String expectedName = Fixture.name();
    final CastMemberType expectedType = null;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'type' should not be null";


    final var aCommand = UpdateCastMemberCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedType
    );
    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

    // then

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(actualException.getErrors().size(), expectedErrorCount);
    Assertions.assertEquals(actualException.getErrors().get(0).message(), expectedErrorMessage);

    verify(castMemberGateway).findByID(any());

    verify(castMemberGateway, times(0)).update(any());
  }

  @Test
  public void givenAInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
    // given
    final var expectedId = CastMemberID.from("invalid-id");
    final String expectedName = Fixture.name();
    final var expectedType = Fixture.CastMembers.type();
    final var expectedErrorMessage = "CastMember with ID invalid-id was not found";


    final var aCommand = UpdateCastMemberCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedType
    );
    // when
    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

    // then

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(actualException.getMessage(), expectedErrorMessage);

    verify(castMemberGateway).findByID(eq(expectedId));

    verify(castMemberGateway, times(0)).update(any());
  }
}