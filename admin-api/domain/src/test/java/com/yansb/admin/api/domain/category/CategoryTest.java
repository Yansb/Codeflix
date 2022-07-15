package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.category.Category;
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
}
