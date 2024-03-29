package com.yansb.admin.api.application.category.delete;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteCategoryUseCaseTest extends UseCaseTest {
  @InjectMocks
  private DefaultDeleteCategoryUseCase useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  @Test
  public void givenAValidID_whenCallsDeleteCategory_shouldBeOk() {
    final var aCategory = Category.newCategory("Movies", "Most watched category", true);

    final var expectedId = aCategory.getId();

    doNothing().when(categoryGateway).deleteById(eq(expectedId));

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
  }

  @Test
  public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk() {
    final var expectedId = CategoryID.from("invalidId");

    doNothing().when(categoryGateway).deleteById(eq(expectedId));

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
  }

  @Test
  public void givenAnId_whenGatewayThrowsError_shouldThrowException() {
    final var expectedId = CategoryID.from("invalidId");

    doThrow(new IllegalStateException(("Gateway error;"))).when(categoryGateway).deleteById(eq(expectedId));

    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
  }
}
