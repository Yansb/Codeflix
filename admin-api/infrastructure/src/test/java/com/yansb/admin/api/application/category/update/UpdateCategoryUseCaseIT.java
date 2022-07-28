package com.yansb.admin.api.application.category.update;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseIT {
  @Autowired
  private UpdateCategoryUseCase useCase;
  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;

  @Test
  public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
    final var aCategory = Category.newCategory("Movi", "Most wa", true);

    save(aCategory);

    final var expectedId = aCategory.getId();
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;

    final var aCommand = UpdateCategoryCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    Assertions.assertEquals(1, categoryRepository.count());
    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    final var actualCategory =
        categoryRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull( actualCategory.getCreatedAt());
    Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
    Assertions.assertNull(actualCategory.getDeletedAt());
  }

  @Test
  public void givenAInvalidName_whenCallsUpdateCategory_thenReturnDomainException(){
    final var aCategory = Category.newCategory("Movi", "Most wa", true);

    save(aCategory);

    final String expectedName = null;
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand =
        UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    final var notification = useCase.execute(aCommand).getLeft();


    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

    verify(categoryGateway, times(0)).update(any());
  }

  @Test
  public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
    final var aCategory = Category.newCategory("Movi", "Most wa", true);

    save(aCategory);

    final var expectedId = aCategory.getId();
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = false;


    final var aCommand = UpdateCategoryCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    Assertions.assertTrue(aCategory.getIsActive());
    Assertions.assertNull(aCategory.getDeletedAt());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    final var actualCategory =
        categoryRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull( actualCategory.getCreatedAt());
    Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
    Assertions.assertNotNull(actualCategory.getDeletedAt());
  }

  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
    final var aCategory =
        Category.newCategory("Movi", "Most wa", true);

    save(aCategory);

    final var expectedId = aCategory.getId();
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "Gateway error";
    final var expectedErrorCount = 1;


    final var aCommand =
        UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).update(any());

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

    final var actualCategory =
        categoryRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
    Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
    Assertions.assertEquals(aCategory.getIsActive(), actualCategory.isActive());
    Assertions.assertNotNull( actualCategory.getCreatedAt());
    Assertions.assertNotNull(aCategory.getUpdatedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());

  }

  @Test
  public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldThrowNotFound(){
    final var expectedId = "123";
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "Category with ID 123 was not found";
    final var expectedErrorCount = 1;


    final var aCommand = UpdateCategoryCommand.with(
        expectedId,
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var actualException =
        Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }

  private void save(final Category... aCategory) {
    categoryRepository.saveAllAndFlush(
        Arrays.stream(aCategory)
            .map(CategoryJpaEntity::from)
            .toList()
    );
  }

}
