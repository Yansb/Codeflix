package com.yansb.admin.api.application.genre.retrieve.get;

import com.yansb.admin.api.IntegrationTest;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private DefaultGetGenreByIdUseCase getGenreByIdUseCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        // given
        final var shows =
                categoryGateway.create(Category.newCategory("shows", null, true));

        final var movies =
                categoryGateway.create(Category.newCategory("movies", null, true));

        final var expectedName = "Action";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                shows.getId(),
                movies.getId()
        );

        final var aGenre = genreGateway.create(Genre
                .newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories));

        final var expectedId = aGenre.getId();

        // when
        final var actualGenre = getGenreByIdUseCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size() &&
                asString(expectedCategories).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndGenreDoesNotExists_shouldReturnNotFound() {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var anId = GenreID.from("123");

        // when
        final var actualException = Assertions.assertThrows(NotFoundException.class,
                () -> getGenreByIdUseCase.execute(anId.getValue())
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(List<CategoryID> ids) {
        return ids.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
