package com.yansb.admin.api.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yansb.admin.api.ApiTest;
import com.yansb.admin.api.ControllerTest;
import com.yansb.admin.api.application.category.create.CreateCategoryOutput;
import com.yansb.admin.api.application.category.create.CreateCategoryUseCase;
import com.yansb.admin.api.application.category.delete.DeleteCategoryUseCase;
import com.yansb.admin.api.application.category.retrieve.get.CategoryOutput;
import com.yansb.admin.api.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.yansb.admin.api.application.category.retrieve.list.CategoryListOutput;
import com.yansb.admin.api.application.category.retrieve.list.ListCategoriesUseCase;
import com.yansb.admin.api.application.category.update.UpdateCategoryOutput;
import com.yansb.admin.api.application.category.update.UpdateCategoryUseCase;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.handler.Notification;
import com.yansb.admin.api.infrastructure.category.models.CreateCategoryRequest;
import com.yansb.admin.api.infrastructure.category.models.UpdateCategoryRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static java.util.List.of;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched categories";
        final var expectedIsActive = true;

        //when
        final var aInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(Right(CreateCategoryOutput.from("123")));

        final var request = post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
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
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final var request = post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
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
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        // when
        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final var request = post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
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
                .with(ApiTest.CATEGORIES_JWT)
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
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        //when

        final var request = get("/categories/{id}", expectedId.getValue())
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        //given
        final var expectedId = "123";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final var aCommand =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_thenReturnNotFound() throws Exception {
        //given
        final var expectedId = "not-found";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenReturnDomainException() throws Exception {
        //given
        final var expectedId = "123";
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var aCommand =
                new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        //when
        final var request = put("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeReturnNoContent() throws Exception {
        //given
        final var expectedId = "123";

        doNothing()
                .when(deleteCategoryUseCase).execute(any());


        //when
        final var request = delete("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        //given
        final var aCategory = Category.newCategory("Movies", "Most watched category", true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "descriptions";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final java.util.List<CategoryListOutput> expectedItems = of(CategoryListOutput.from(aCategory));

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<CategoryListOutput>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        //when
        final var request = get("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        //then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.getIsActive())));

        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
