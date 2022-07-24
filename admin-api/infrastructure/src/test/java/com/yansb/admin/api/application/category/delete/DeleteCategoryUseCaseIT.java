package com.yansb.admin.api.application.category.delete;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

  @Autowired
  private DeleteCategoryUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @SpyBean
  private CategoryGateway categoryGateway;

  @Test
  public void givenAValidID_whenCallsDeleteCategory_shouldBeOk(){
    final var aCategory = Category.newCategory("Movies", "Most watched category", true);

    final var expectedId = aCategory.getId();

    save(aCategory);

    Assertions.assertEquals(1, categoryRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Assertions.assertEquals(0, categoryRepository.count());

    Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
  }

  @Test
  public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk(){
    final var expectedId = CategoryID.from("invalidId");

    Assertions.assertEquals(0, categoryRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
  }

  @Test
  public void givenAnId_whenGatewayThrowsError_shouldThrowException(){
    final var expectedId = CategoryID.from("invalidId");

    doThrow(new IllegalStateException(("Gateway error;"))).when(categoryGateway).deleteById(eq(expectedId));

    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
  }

  private void save(final Category... aCategory) {
    categoryRepository.saveAllAndFlush(
        Arrays.stream(aCategory)
            .map(CategoryJpaEntity::from)
            .toList()
    );
  }
}
