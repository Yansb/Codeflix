package com.yansb.admin.api.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yansb.admin.api.ControllerTest;
import com.yansb.admin.api.Fixture;
import com.yansb.admin.api.application.castMember.create.CreateCastMemberOutput;
import com.yansb.admin.api.application.castMember.create.DefaultCreateCastMemberUseCase;
import com.yansb.admin.api.application.castMember.delete.DefaultDeleteCastMemberUseCase;
import com.yansb.admin.api.application.castMember.retrieve.get.CastMemberOutput;
import com.yansb.admin.api.application.castMember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.yansb.admin.api.application.castMember.retrieve.list.DefaultListCastMemberUseCase;
import com.yansb.admin.api.application.castMember.update.DefaultUpdateCastMemberUseCase;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.infrastructure.castMember.models.CreateCastMemberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {
  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private DefaultCreateCastMemberUseCase createCastMemberUseCase;

  @MockBean
  private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

  @MockBean
  private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

  @MockBean
  private DefaultListCastMemberUseCase listCastMemberUseCase;

  @MockBean
  private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

  @Test
  public void givenAValidCommand_whenCallsCreateCastMember_shouldReturnItsIdentifier() throws Exception {
    // given
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();
    final var expectedId = CastMemberID.from(Fixture.id());

    final var aCommand =
        new CreateCastMemberRequest(expectedName, expectedType);

    when(createCastMemberUseCase.execute(any()))
        .thenReturn(CreateCastMemberOutput.from(expectedId));

    // when
    final var aRequest = post("/cast_members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(aRequest)
        .andDo(print());
    // then
    response.andExpect(status().isCreated())
        .andExpect(header().string("Location", "/cast_members/" + expectedId.getValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));
    verify(createCastMemberUseCase).execute(argThat(actualCmd ->
        Objects.equals(actualCmd.name(), expectedName) &&
            Objects.equals(actualCmd.type(), expectedType)));
  }

  @Test
  public void givenAnInvalidName_whenCallsCreateCastMember_shouldReturnNotification() throws Exception {
    // given
    final String expectedName = null;
    final var expectedType = Fixture.CastMember.type();
    final var expectedErrorMessage = "'name' should not be null";
    final var aCommand =
        new CreateCastMemberRequest(expectedName, expectedType);

    when(createCastMemberUseCase.execute(any()))
        .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

    // when
    final var aRequest = post("/cast_members")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(aRequest)
        .andDo(print());
    // then
    response.andExpect(status().isUnprocessableEntity())
        .andExpect(header().string("Location", nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
    verify(createCastMemberUseCase).execute(argThat(actualCmd ->
        Objects.equals(actualCmd.name(), expectedName) &&
            Objects.equals(actualCmd.type(), expectedType)));
  }

  @Test
  public void givenAValidId_whenCallsGetById_shouldReturnIt() throws Exception {
    // given
    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();

    final var aMember = CastMember.newMember(expectedName, expectedType);
    final var expectedId = aMember.getId();

    when(getCastMemberByIdUseCase.execute(any()))
        .thenReturn(CastMemberOutput.from(aMember));

    // when
    final var aRequest = get("/cast_members/{id}", expectedId.getValue())
        .accept(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(aRequest)
        .andDo(print());

    // then
    response.andExpect(status().isOk())
        .andExpect(header().string("Location", nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())))
        .andExpect(jsonPath("$.name", equalTo(expectedName)))
        .andExpect(jsonPath("$.type", equalTo(expectedType.name())))
        .andExpect(jsonPath("$.created_at", equalTo(aMember.getCreatedAt().toString())))
        .andExpect(jsonPath("$.updated_at", equalTo(aMember.getUpdatedAt().toString())));

    verify(getCastMemberByIdUseCase).execute(eq(expectedId.getValue()));
  }

  @Test
  public void givenAInvalidId_whenCallsGetByIdAndCastMemberDoesntExists_shouldThrowNotFound() throws Exception {
    // given
    final var expectedErrorMessage = "CastMember with ID 123 was not found";
    final var expectedId = CastMemberID.from("123");

    when(getCastMemberByIdUseCase.execute(any()))
        .thenThrow(NotFoundException.with(CastMember.class, expectedId));

    // when
    final var aRequest = get("/cast_members/{id}", expectedId.getValue())
        .accept(MediaType.APPLICATION_JSON);

    final var response = this.mvc.perform(aRequest)
        .andDo(print());

    // then
    response.andExpect(status().isNotFound())
        .andExpect(header().string("Location", nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

    verify(getCastMemberByIdUseCase).execute(eq(expectedId.getValue()));
  }
}
