package com.yansb.admin.api.application.category.retrieve.get;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

  @InjectMocks
  private DefaultGetCategoryByIdUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @BeforeEach
  void cleanUp() {
    reset(categoryGateway);
  }
  
  @Test
  public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;
    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);


    CategoryID expectedId = aCategory.getId();

    when(categoryGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(aCategory.clone()));

    final var actualCategory = useCase.execute(expectedId.getValue());

    Assertions.assertEquals(CategoryOutput.from(aCategory), actualCategory);
    Assertions.assertEquals(expectedDescription, actualCategory.description());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertEquals(aCategory.getId(), actualCategory.id());
    Assertions.assertEquals(actualCategory.createdAt(), aCategory.getCreatedAt());
    Assertions.assertEquals(actualCategory.updatedAt(), aCategory.getUpdatedAt());
    Assertions.assertEquals(expectedName, actualCategory.name());
  }
  
  @Test
  public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
    final var expectedErrorMessage ="Category with ID 123 was not found";
    CategoryID expectedId = CategoryID.from("123");

    when(categoryGateway.findById(eq(expectedId)))
        .thenReturn(Optional.empty());

    final var actualException = Assertions.assertThrows(
        NotFoundException.class,
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

    when(categoryGateway.findById(eq(expectedId)))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var actualException = Assertions.assertThrows(
        IllegalStateException.class,
        () -> useCase.execute(expectedId.getValue())
    );

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }
}
