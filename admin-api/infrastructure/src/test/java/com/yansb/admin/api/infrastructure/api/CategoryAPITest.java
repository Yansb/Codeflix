package com.yansb.admin.api.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yansb.admin.api.ControllerTest;
import com.yansb.admin.api.application.category.create.CreateCategoryOutput;
import com.yansb.admin.api.application.category.create.CreateCategoryUseCase;
import com.yansb.admin.api.application.category.retrieve.get.CategoryOutput;
import com.yansb.admin.api.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.handler.Notification;
import com.yansb.admin.api.infrastructure.category.models.CreateCategoryApiInput;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryJpaEntity;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private CreateCategoryUseCase createCategoryUseCase;

  @MockBean
  private GetCategoryByIdUseCase getCategoryByIdUseCase;

  @Test
  public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
    // given
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;

    //when
    final var aInput =
        new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

    Mockito.when(createCategoryUseCase.execute(Mockito.any()))
        .thenReturn(Right(CreateCategoryOutput.from("123")));

    final var request = post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(aInput));

    // then
    this.mvc.perform(request)
        .andDo(print())
        .andExpectAll(
            status().isCreated(),
            header().string("Location", "/categories/123"),
            header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        );

    Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
        && Objects.equals(expectedDescription, cmd.description())
            && Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAInvalidName_whenCallsCreateCategory_thenReturnNotificationException() throws Exception {
    //given
    final String expectedName = null;
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedMessage = "'name' should not be null";

    final var aInput =
        new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

    //when
    Mockito.when(createCategoryUseCase.execute(Mockito.any()))
        .thenReturn(Left(Notification.create(new Error(expectedMessage))));

    final var request = post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(aInput));

    // then
    this.mvc.perform(request)
        .andDo(print())
        .andExpect(status().isUnprocessableEntity())
        .andExpect(header().string("Location", Matchers.nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

    Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
        && Objects.equals(expectedDescription, cmd.description())
            && Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAInvalidCommand_whenCallsCreateCategory_thenReturnDomainException() throws Exception {
    //given
    final String expectedName = null;
    final var expectedDescription = "Most watched categories";
    final var expectedIsActive = true;
    final var expectedMessage = "'name' should not be null";

    final var aInput =
        new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

    // when
    Mockito.when(createCategoryUseCase.execute(Mockito.any()))
        .thenThrow(DomainException.with(new Error(expectedMessage)));

    final var request = post("/categories")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.mapper.writeValueAsString(aInput));

    final var response = this.mvc.perform(request)
        .andDo(print());
    // then
    response.andExpect(status().isUnprocessableEntity())
    .andExpect(header().string("Location", Matchers.nullValue()))
    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
    .andExpect(jsonPath("$.message", equalTo(expectedMessage)))
    .andExpect(jsonPath("$.errors", hasSize(1)))
    .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

    Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
        Objects.equals(expectedName, cmd.name())
        && Objects.equals(expectedDescription, cmd.description())
            && Objects.equals(expectedIsActive, cmd.isActive())
    ));
  }

  @Test
  public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
    //given
    final var expectedName = "Movies";
    final var expectedDescription = "Most watched category";
    final var expectedIsActive = true;
    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);


    final var expectedId = aCategory.getId().getValue();

    when(getCategoryByIdUseCase.execute(any()))
        .thenReturn(CategoryOutput.from(aCategory));
    //when
    final var request = get("/categories/{id}", expectedId)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(request)
        .andDo(print());

    //then
    response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)))
            .andExpect(jsonPath("$.name", equalTo(expectedName)))
            .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
            .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
            .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
            .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
            .andExpect(jsonPath("$.deleted_at", equalTo(null)));

    verify(getCategoryByIdUseCase, times(1)).execute(expectedId);
  }

  @Test
  public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
    //given
    final var expectedErrorMessage ="Category with ID 123 was not found";
    final var expectedId = CategoryID.from("123");

    when(getCategoryByIdUseCase.execute(any()))
        .thenThrow(NotFoundException.with(Category.class, expectedId));

    //when

    final var request = get("/categories/{id}", expectedId.getValue())
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(request)
        .andDo(print());

    // then
    response.andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
  }
}
