package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {
  @Test
  public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    Assertions.assertNotNull(actualCategory);
    Assertions.assertNotNull(actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectIsActive, actualCategory.getIsActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());
  }
  @Test
  public void givenAnInvalidNullName_whenCallNewCategoryValidate_thenShouldReceiveError(){
    final String expectedName = null;
    final var expectedErrorMessage= "'name' should not be null";
    final var expectedErrorCount = 1;
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

  }

  @Test
  public void givenAnInvalidEmptyName_whenCallNewCategoryValidate_thenShouldReceiveError(){
    final String expectedName = "  ";
    final var expectedErrorMessage= "'name' should not be empty";
    final var expectedErrorCount = 1;
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

  }
  @Test
  public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryValidate_thenShouldReceiveError(){
    final String expectedName = "Fi ";
    final var expectedErrorMessage= "'name' must be between 3 and 255 characters";
    final var expectedErrorCount = 1;
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

  }
  @Test
  public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryValidate_thenShouldReceiveError(){
    final String expectedName = """
      Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Bibendum arcu vitae elementum curabitur vitae nunc. 
      A iaculis at erat pellentesque adipiscing commodo elit at imperdiet. Id venenatis a condimentum vitae sapien pellentesque habitant morbi tristique. Mauris pellentesque pulvinar pellentesque habitant morbi. 
      Consectetur a erat nam at lectus urna duis convallis convallis. In fermentum posuere urna nec. Penatibus et magnis dis parturient montes nascetur ridiculus. Magnis dis parturient montes nascetur ridiculus mus. 
      Ultrices gravida dictum fusce ut placerat orci nulla. Orci eu lobortis elementum nibh tellus molestie nunc. Justo donec enim diam vulputate ut pharetra sit. Commodo nulla facilisi nullam vehicula ipsum a arcu.
    """;
    final var expectedErrorMessage= "'name' must be between 3 and 255 characters";
    final var expectedErrorCount = 1;
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

  }
  @Test
  public void givenAnValidEmptyDescription_whenCallNewCategoryValidate_thenShouldCreateCategory(){
    final String expectedName = "Valid name";
    final var expectedDescription = " ";
    final var expectIsActive = true;

    final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

    Assertions.assertNotNull(actualCategory);
    Assertions.assertNotNull(actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectIsActive, actualCategory.getIsActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());
  }
  @Test
  public void givenAnValidFalseIsActive_whenCallNewCategoryValidate_thenShouldCreateCategory(){
    final String expectedName = "Valid name";
    final var expectedDescription = "Most watched category";
    final var expectIsActive = false;

    final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

    Assertions.assertNotNull(actualCategory);
    Assertions.assertNotNull(actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectIsActive, actualCategory.getIsActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNotNull(actualCategory.getDeletedAt());
  }

  @Test
  public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivate(){
    final String expectedName = "Valid name";
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

    final var createdAt = aCategory.getCreatedAt();
    final var updatedAt = aCategory.getUpdatedAt();

    Assertions.assertNull(aCategory.getDeletedAt());
    Assertions.assertTrue(aCategory.getIsActive());

    final var actualCategory = aCategory.deactivate();


    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertFalse(actualCategory.getIsActive());
    Assertions.assertEquals(createdAt,actualCategory.getCreatedAt());
    Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertNotNull(actualCategory.getDeletedAt());

  }

  @Test
  public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryInactivate(){
    final String expectedName = "Valid name";
    final var expectedDescription = "Most watched category";
    final var expectIsActive = false;

    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectIsActive);

    Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

    final var updatedAt = aCategory.getUpdatedAt();
    final var createdAt = aCategory.getCreatedAt();

    Assertions.assertNotNull(aCategory.getDeletedAt());
    Assertions.assertFalse(aCategory.getIsActive());

    final var actualCategory = aCategory.activate();


    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertTrue(actualCategory.getIsActive());
    Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
    Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertNull(actualCategory.getDeletedAt());
  }

  @Test
  public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated(){
    final String expectedName = "Films";
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var aCategory = Category.newCategory("Film", "category", expectIsActive);

    Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

    final var updatedAt = aCategory.getUpdatedAt();
    final var createdAt = aCategory.getCreatedAt();

    final var actualCategory = aCategory.update(expectedName, expectedDescription, expectIsActive);

    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertTrue(actualCategory.getIsActive());
    Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
    Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertNull(actualCategory.getDeletedAt());
  }
  @Test
  public void givenAValidCategory_whenUpdateToInactive_thenReturnCategoryUpdated(){
    final String expectedName = "Films";
    final var expectedDescription = "Most watched category";
    final var expectIsActive = false;

    final var aCategory = Category.newCategory("Film", "category", true);

    Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));
    Assertions.assertNull(aCategory.getDeletedAt());
    Assertions.assertTrue(aCategory.getIsActive());
    final var updatedAt = aCategory.getUpdatedAt();
    final var createdAt = aCategory.getCreatedAt();

    final var actualCategory = aCategory.update(expectedName, expectedDescription, expectIsActive);

    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertFalse(actualCategory.getIsActive());
    Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
    Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertNotNull(actualCategory.getDeletedAt());
  }
  @Test
  public void givenAValidCategory_whenUpdateWithInvalidParams_thenReturnCategory(){
    final String expectedName = null;
    final var expectedDescription = "Most watched category";
    final var expectIsActive = true;

    final var aCategory = Category.newCategory("Film", "category", true);

    Assertions.assertDoesNotThrow(() -> aCategory.validate(new ThrowsValidationHandler()));

    final var updatedAt = aCategory.getUpdatedAt();
    final var createdAt = aCategory.getCreatedAt();

    final var actualCategory = aCategory.update(expectedName, expectedDescription, expectIsActive);

    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertTrue(actualCategory.getIsActive());
    Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
    Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
    Assertions.assertNull(actualCategory.getDeletedAt());
  }
}
