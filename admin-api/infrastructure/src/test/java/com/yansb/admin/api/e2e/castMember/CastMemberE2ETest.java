package com.yansb.admin.api.e2e.castMember;


import com.yansb.admin.api.E2ETest;
import com.yansb.admin.api.Fixture;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.castMember.CastMemberType;
import com.yansb.admin.api.e2e.MockDsl;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

  @Container
  private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
      .withPassword("123456")
      .withUsername("root")
      .withDatabaseName("adm_videos");
  @Autowired
  private MockMvc mvc;
  @Autowired
  private CastMemberRepository castMemberRepository;

  @DynamicPropertySource
  public static void setDataSourceProperties(final DynamicPropertyRegistry registry) {
    registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
  }

  @Override
  public MockMvc mvc() {
    return this.mvc;
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToCreateANewCastMember_withValidValues() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();

    final var actualMemberId = givenACastMember(expectedName, expectedType);

    final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();

    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertNotNull(actualMember.getCreatedAt());
    Assertions.assertNotNull(actualMember.getUpdatedAt());
    Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
  }

  @Test
  public void asACatalogAdmin_iShouldNotBeAbleToCreateANewCastMember_withInvalidValues() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    final String expectedName = null;
    final var expectedType = Fixture.CastMember.type();
    final var expectedErrorMessage = "'name' should not be null";

    givenACastMemberResult(expectedName, expectedType)
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(header().string("Location", nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToNavigateThroughAllMembers() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    givenACastMember("Vin Diesel", CastMemberType.ACTOR);
    givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
    givenACastMember("Jason Momoa", CastMemberType.ACTOR);

    listCastMembers(
        0,
        1
    ).andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Jason Momoa")));

    listCastMembers(
        1,
        1
    ).andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(1)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Quentin Tarantino")));

    listCastMembers(
        2,
        1
    ).andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(2)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

    listCastMembers(
        3,
        1
    ).andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(3)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(0)));

  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToSearchThroughAllMembers() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    givenACastMember("Vin Diesel", CastMemberType.ACTOR);
    givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
    givenACastMember("Jason Momoa", CastMemberType.ACTOR);

    listCastMembers(
        0,
        1,
        "vin"
    ).andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(1)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToOrdenateAllMembersByNameDesc() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    givenACastMember("Vin Diesel", CastMemberType.ACTOR);
    givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
    givenACastMember("Jason Momoa", CastMemberType.ACTOR);

    listCastMembers(
        0,
        3,
        "",
        "name",
        "desc"
    ).andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(3)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(3)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")))
        .andExpect(jsonPath("$.items[1].name", equalTo("Quentin Tarantino")))
        .andExpect(jsonPath("$.items[2].name", equalTo("Jason Momoa")));
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToGetACasMemberByItsIdentifier() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();

    givenACastMember(Fixture.name(), Fixture.CastMember.type());
    final var castMemberId = givenACastMember(expectedName, expectedType);

    final var actualMember = retrieveACastMember(castMemberId);

    Assertions.assertEquals(expectedName, actualMember.name());
    Assertions.assertEquals(expectedType, actualMember.type());
    Assertions.assertNotNull(actualMember.created_at());
    Assertions.assertNotNull(actualMember.updated_at());
    Assertions.assertEquals(actualMember.created_at(), actualMember.updated_at());
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToSeeATreatedErrorByGettingANotFoundCastMember() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    givenACastMember(Fixture.name(), Fixture.CastMember.type());
    final var castMemberId = CastMemberID.from("invalid-id");

    retrieveACastMemberResult(castMemberId)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", equalTo("CastMember with ID invalid-id was not found")));
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToUpdateACastMemberByItsIdentifier() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    final var expectedName = "Vin Diesel";
    final var expectedType = CastMemberType.ACTOR;

    final var castMemberId = givenACastMember("vin d", CastMemberType.DIRECTOR);

    updateACastMember(castMemberId, expectedName, expectedType)
        .andExpect(status().isOk());

    final var actualMember = retrieveACastMember(castMemberId);

    Assertions.assertEquals(expectedName, actualMember.name());
    Assertions.assertEquals(expectedType, actualMember.type());
    Assertions.assertNotNull(actualMember.created_at());
    Assertions.assertNotNull(actualMember.updated_at());
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToSeeATreatedErrorByUpdatingACastMemberWithInvalidValues() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());

    final var expectedName = "";
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorMessage = "'name' should not be empty";

    final var castMemberId = givenACastMember("vin d", CastMemberType.DIRECTOR);

    updateACastMember(castMemberId, expectedName, expectedType)
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.errors", hasSize(2)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToDeleteACastMemberByItsIdentifier() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());


    givenACastMember(Fixture.name(), Fixture.CastMember.type());
    final var castMemberId = givenACastMember(Fixture.name(), Fixture.CastMember.type());

    Assertions.assertEquals(2, this.castMemberRepository.count());

    deleteACastMember(castMemberId)
        .andExpect(status().isNoContent());

    Assertions.assertEquals(1, this.castMemberRepository.count());
    Assertions.assertFalse(this.castMemberRepository.existsById(castMemberId.getValue()));
  }

  @Test
  public void asACatalogAdmin_iShouldBeAbleToDeleteACastMemberWithInvalidID() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, this.castMemberRepository.count());


    givenACastMember(Fixture.name(), Fixture.CastMember.type());
    final var castMemberId = CastMemberID.from("invalid-id");

    Assertions.assertEquals(1, this.castMemberRepository.count());

    deleteACastMember(castMemberId)
        .andExpect(status().isNoContent());

    Assertions.assertEquals(1, this.castMemberRepository.count());
    Assertions.assertFalse(this.castMemberRepository.existsById(castMemberId.getValue()));
  }


}
