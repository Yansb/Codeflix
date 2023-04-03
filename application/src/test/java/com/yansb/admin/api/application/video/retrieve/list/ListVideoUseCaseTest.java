package com.yansb.admin.api.application.video.retrieve.list;

import com.yansb.admin.api.application.UseCaseTest;
import com.yansb.admin.api.domain.Fixture;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.video.VideoGateway;
import com.yansb.admin.api.domain.video.VideoPreview;
import com.yansb.admin.api.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListVideosUseCase useCase;

    @Mock
    private VideoGateway videoGateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListVideos_shouldReturnVideos() {
        // given
        final var videos = List.of(
                Fixture.aVideoPreview(),
                Fixture.aVideoPreview()
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedOrder = "asc";
        final var expectedTotal = 2;
        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedOrder, Set.of(), Set.of(), Set.of());

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListVideoAndGatewayThrowsError_shouldThrowError() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedOrder = "asc";

        String expectedMessage = "Gateway Error";

        when(videoGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedMessage));

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedOrder, Set.of(), Set.of(), Set.of());

        // when
        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedMessage, actualException.getMessage());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListVideoAndResultIsEmpty_shouldReturnGenreList() {
        // given
        final var videos = List.<VideoPreview>of(
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedOrder = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<VideoListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                videos
        );

        when(videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new VideoSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedOrder, Set.of(), Set.of(), Set.of());

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(videoGateway, times(1)).findAll(eq(aQuery));
    }
}
