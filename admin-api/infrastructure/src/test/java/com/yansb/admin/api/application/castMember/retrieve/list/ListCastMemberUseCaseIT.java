package com.yansb.admin.api.application.castMember.retrieve.list;

import com.yansb.admin.api.Fixture;
import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberJpaEntity;
import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.Mockito.*;

@IntegrationTest
public class ListCastMemberUseCaseIT {

  @Autowired
  private DefaultListCastMemberUseCase listCastMemberUseCase;

  @Autowired
  private CastMemberRepository castMemberRepository;

  @SpyBean
  private CastMemberGateway castMemberGateway;

  @Test
  public void givenAValidQuery_whenCallsListCastMember_shouldReturnAll() {
    // given
    final var members = List.of(
        CastMember.newMember(Fixture.name(), Fixture.CastMember.type()),
        CastMember.newMember(Fixture.name(), Fixture.CastMember.type())
    );

    castMemberRepository.saveAll(members.stream().map(CastMemberJpaEntity::from).toList());

    Assertions.assertEquals(2, this.castMemberRepository.count());
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = members.stream()
        .map(CastMemberListOutput::from)
        .toList();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    // when
    final var actualOutput = listCastMemberUseCase.execute(aQuery);
    // then

    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());
    Assertions.assertTrue(expectedItems.size() == actualOutput.items().size()
        && expectedItems.containsAll(actualOutput.items())
        && actualOutput.items().containsAll(expectedItems));

    verify(castMemberGateway).findAll(eq(aQuery));
  }

  @Test
  public void givenAValidQuery_whenCallsListCastMemberAndIsEmpty_shouldReturnEmpty() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "Something";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var expectedList = List.<CastMemberListOutput>of();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    // when
    final var actualOutput = listCastMemberUseCase.execute(aQuery);
    // then

    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedList, actualOutput.items());

    verify(castMemberGateway).findAll(any());
  }

  @Test
  public void givenAValidQuery_whenCallsListCastMemberAndGatewayThrowsRandomException_shouldThrowException() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var expectedErrorMessage = "Random exception";

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(castMemberGateway)
        .findAll(any());

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    // when
    final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> listCastMemberUseCase.execute(aQuery));
    // then

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(castMemberGateway).findAll(any());
  }
}
