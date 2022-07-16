package com.yansb.admin.api.application.category.create;

import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

  @InjectMocks
  private DefaultCreateCategoryUseCase useCase;

  @Mock
  private CategoryGateway gateway;

  @Test
  public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;


    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    when(gateway.create(any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    verify(gateway, times(1))
        .create(argThat(aCategory-> Objects.equals(expectedName, aCategory.getName())
                  && Objects.equals(expectedDescription, aCategory.getDescription())
                  && Objects.equals(expectedIsActive, aCategory.getIsActive())
                  && Objects.nonNull(aCategory.getId())
                  && Objects.nonNull(aCategory.getCreatedAt())
                  && Objects.nonNull(aCategory.getUpdatedAt())
                  && Objects.isNull(aCategory.getDeletedAt())
            ));
  }

  @Test
  public void givenAInvalidName_whenCallsCreateCategory_thenReturnDomainException(){
    final String expectedName = null;
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    verify(gateway, times(0)).create(any());
  }

  @Test
  public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_thenReturnInactiveCategoryId(){
    final String expectedName = "Movies";
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = false;

    when(gateway.create(any()))
        .thenAnswer(returnsFirstArg());

    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    verify(gateway, times(1))
        .create(argThat(aCategory-> Objects.equals(expectedName, aCategory.getName())
            && Objects.equals(expectedDescription, aCategory.getDescription())
            && Objects.equals(expectedIsActive, aCategory.getIsActive())
            && Objects.nonNull(aCategory.getId())
            && Objects.nonNull(aCategory.getCreatedAt())
            && Objects.nonNull(aCategory.getUpdatedAt())
            && Objects.nonNull(aCategory.getDeletedAt())
        ));
  }

  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException(){
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "Gateway error";
    final var expectedErrorCount = 1;


    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    when(gateway.create(any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));


    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());

    verify(gateway, times(1)).create(any());

  }
}
