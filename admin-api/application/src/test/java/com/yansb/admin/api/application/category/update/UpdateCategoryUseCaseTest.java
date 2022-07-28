package com.yansb.admin.api.application.category.update;

import com.yansb.admin.api.application.category.create.CreateCategoryCommand;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

  @InjectMocks
  private DefaultUpdateCategoryUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @BeforeEach
  void cleanUp(){
    Mockito.reset(categoryGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
    final var aCategory = Category.newCategory("Movi", "Most wa", true);

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

    when(categoryGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Category.clone(aCategory)));

    when(categoryGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
    Mockito.verify(categoryGateway, times(1)).update(argThat(
        aUpdatedCategory ->
            Objects.equals(expectedName, aUpdatedCategory.getName())
            && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
            && Objects.equals(expectedIsActive, aUpdatedCategory.getIsActive())
            && Objects.equals(expectedId, aUpdatedCategory.getId())
            && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
            && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
            && Objects.isNull(aUpdatedCategory.getDeletedAt())
    ));

  }

  @Test
  public void givenAInvalidName_whenCallsUpdateCategory_thenReturnNotFoundException(){
    final var aCategory = Category.newCategory("Movi", "Most wa", true);

    final String expectedName = null;
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand =
        UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    when(categoryGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Category.clone(aCategory)));

    final var notification = useCase.execute(aCommand).getLeft();


    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

    verify(categoryGateway, times(0)).update(any());
  }

  @Test
  public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
    final var aCategory = Category.newCategory("Movi", "Most wa", true);

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

    when(categoryGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Category.clone(aCategory)));

    when(categoryGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    Assertions.assertTrue(aCategory.getIsActive());
    Assertions.assertNull(aCategory.getDeletedAt());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(categoryGateway, times(1)).findById(eq(expectedId));
    Mockito.verify(categoryGateway, times(1)).update(argThat(
        aUpdatedCategory ->
            Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.getIsActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.nonNull(aUpdatedCategory.getDeletedAt())
    ));
  }

  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
    final var aCategory =
        Category.newCategory("Movi", "Most wa", true);

    final var expectedId = aCategory.getId();
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "Gateway error";
    final var expectedErrorCount = 1;


    final var aCommand =
        UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    when(categoryGateway.findById(eq(expectedId)))
        .thenReturn(Optional.of(Category.clone(aCategory)));

    when(categoryGateway.update(any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));


    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

    verify(categoryGateway, times(1)).update(any());

  }

  @Test
  public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldThrowNotFound(){
    final var expectedId = "123";
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "Category with ID 123 was not found";


    final var aCommand = UpdateCategoryCommand.with(
        expectedId,
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
        .thenReturn(Optional.empty());

    final var actualException =
        Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    Mockito.verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
    Mockito.verify(categoryGateway, times(0)).update(any());

  }
}
