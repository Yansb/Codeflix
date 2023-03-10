package com.yansb.admin.api.application.castMember.retrieve.list;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.*;

public class ListCastMemberUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DefaultListCastMemberUseCase listCastMemberUseCase;

  @Mock
  private CastMemberGateway castMemberGateway;


  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  public void givenAValidQuery_whenCallsListCastMember_shouldReturnAll() {
    // given
    final var members = List.of(
        CastMember.newMember(Fixture.name(), Fixture.CastMembers.type()),
        CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
    );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "Something";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = members.stream()
        .map(CastMemberListOutput::from)
        .toList();

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        members
    );


    when(castMemberGateway.findAll(any()))
        .thenReturn(expectedPagination);

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    // when
    final var actualOutput = listCastMemberUseCase.execute(aQuery);
    // then

    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

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

    final var members = List.<CastMember>of();
    final var expectedList = List.<CastMemberListOutput>of();

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        members
    );


    when(castMemberGateway.findAll(any()))
        .thenReturn(expectedPagination);

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    // when
    final var actualOutput = listCastMemberUseCase.execute(aQuery);
    // then

    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedList, actualOutput.items());

    verify(castMemberGateway).findAll(eq(aQuery));
  }

  @Test
  public void givenAValidQuery_whenCallsListCastMemberAndGatewayThrowsRandomException_shouldThrowException() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "Something";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var expectedErrorMessage = "Random exception";

    when(castMemberGateway.findAll(any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
    // when
    final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> listCastMemberUseCase.execute(aQuery));
    // then

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(castMemberGateway).findAll(eq(aQuery));
  }
}
