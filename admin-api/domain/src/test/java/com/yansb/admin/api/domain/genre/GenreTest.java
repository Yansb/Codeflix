package com.yansb.admin.api.domain.genre;

import com.yansb.admin.api.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenreTest {

  @Test
  public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre(){
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAnError(){
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      Genre.newGenre(expectedName, expectedIsActive);
    });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAnError(){
    final var expectedName = "";
    final var expectedIsActive = true;
    final var expectedErrorCount = 2;
    final var expectedErrorMessage = "'name' should not be empty";
    final var expectedErrorMessage2 = "'name' must be between 1 and 255 characters";

    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      Genre.newGenre(expectedName, expectedIsActive);
    });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    Assertions.assertEquals(expectedErrorMessage2, actualException.getErrors().get(1).message());
  }

  @Test
  public void givenInvalidWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAnError(){
    final var expectedName = """
      Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum arcu vitae elementum curabitur vitae nunc. 
      A iaculis at erat pellentesque adipiscing commodo elit at imperdiet. Id venenatis a condimentum vitae sapien pellentesque habitant morbi tristique. Mauris pellentesque pulvinar pellentesque habitant morbi. 
      Consectetur a erat nam at lectus urna duis convallis convallis. In fermentum posuere urna nec. Penatibus et magnis dis parturient montes nascetur ridiculus. Magnis dis parturient montes nascetur ridiculus mus. 
      Ultrices gravida dictum fusce ut placerat orci nulla. Orci eu lobortis elementum nibh tellus molestie nunc. Justo donec enim diam vulputate ut pharetra sit. Commodo nulla facilisi nullam vehicula ipsum a arcu.
    """;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      Genre.newGenre(expectedName, expectedIsActive);
    });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAActiveGenre_whenCallDeactivate_shouldReceiveOk(){
    final var expectedName = "Action";
    final var expectedIsActive = false;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, true);

    Assertions.assertTrue(actualGenre.isActive());
    Assertions.assertNull(actualGenre.getDeletedAt());

    actualGenre.deactivate();

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNotNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOk(){
    final var expectedName = "Action";
    final var expectedIsActive = true;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, false);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertFalse(actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    actualGenre.activate();

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertEquals(actualCreatedAt,actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());
  }
}
