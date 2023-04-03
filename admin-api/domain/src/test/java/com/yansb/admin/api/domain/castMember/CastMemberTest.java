package com.yansb.admin.api.domain.castMember;

import com.yansb.admin.api.domain.UnitTest;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        final var expectedName = "John Doe";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 2;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidNameWithLengthGreaterThen255_whenCallsNewMember_shouldReceiveANotification() {
        final var expectedName = """
                  Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum arcu vitae elementum curabitur vitae nunc. 
                  A iaculis at erat pellentesque adipiscing commodo elit at imperdiet. Id venenatis a condimentum vitae sapien pellentesque habitant morbi tristique. Mauris pellentesque pulvinar pellentesque habitant morbi. 
                  Consectetur a erat nam at lectus urna duis convallis convallis. In fermentum posuere urna nec. Penatibus et magnis dis parturient montes nascetur ridiculus. Magnis dis parturient montes nascetur ridiculus mus. 
                  Ultrices gravida dictum fusce ut placerat orci nulla. Orci eu lobortis elementum nibh tellus molestie nunc. Justo donec enim diam vulputate ut pharetra sit. Commodo nulla facilisi nullam vehicula ipsum a arcu.
                """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
        final String expectedName = "John Doe";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException = Assertions.assertThrows(NotificationException.class,
                () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        final var expectedName = "John Doe";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("John", expectedType);

        Assertions.assertNotNull(actualMember);

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();
        final var actualId = actualMember.getId();

        actualMember.update(expectedName, expectedType);

        Assertions.assertEquals(actualMember.getId(), actualId);
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualCreatedAt);
        Assertions.assertTrue(actualMember.getUpdatedAt().isAfter(actualUpdatedAt));
        Assertions.assertNotEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithNullName_shouldReceiveAnError() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualMember = CastMember.newMember("John", expectedType);

        Assertions.assertNotNull(actualMember);

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithEmptyName_shouldReceiveAnError() {
        final String expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 2;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorMessage2 = "'name' must be between 1 and 255 characters";

        final var actualMember = CastMember.newMember("John", expectedType);

        Assertions.assertNotNull(actualMember);

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithNullType_shouldReceiveAnError() {
        final String expectedName = "John Doe";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember = CastMember.newMember("John", CastMemberType.ACTOR);

        Assertions.assertNotNull(actualMember);

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> actualMember.update(expectedName, expectedType)
        );

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}
