package com.yansb.admin.api.application.category.retrieve.get;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {
  @Autowired
  private GetCategoryByIdUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;

  @Test
  public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;
    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);


    CategoryID expectedId = aCategory.getId();

    save(aCategory);

    final var actualCategory = useCase.execute(expectedId.getValue());

    Assertions.assertEquals(expectedDescription, actualCategory.description());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertEquals(aCategory.getId(), actualCategory.id());
    Assertions.assertNotNull(actualCategory.createdAt());
    Assertions.assertNotNull(actualCategory.updatedAt());
    Assertions.assertEquals(expectedName, actualCategory.name());
  }

  private void save(final Category... aCategory) {
    categoryRepository.saveAllAndFlush(
        Arrays.stream(aCategory)
        .map(CategoryJpaEntity::from)
        .toList()
    );
  }

  @Test
  public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
    final var expectedErrorMessage ="Category with ID 123 was not found";
    CategoryID expectedId = CategoryID.from("123");

    final var actualException = Assertions.assertThrows(
        DomainException.class,
        () -> useCase.execute(expectedId.getValue())
    );

    Assertions.assertEquals(
        expectedErrorMessage,
        actualException.getMessage()
    );
  }

  @Test
  public void givenAnId_whenGatewayThrowsError_shouldThrowException(){
    final var expectedErrorMessage ="Gateway error";
    final var expectedId = CategoryID.from("123");

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(categoryGateway)
        .findById(eq(expectedId));

    final var actualException = Assertions.assertThrows(
        IllegalStateException.class,
        () -> useCase.execute(expectedId.getValue())
    );

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }
}
