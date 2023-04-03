package com.yansb.admin.api.application.genre.update;

import com.yansb.admin.api.domain.Identifier;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(final CategoryGateway categoryGateway,
                                     final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(UpdateGenreCommand anInput) {
        final var anId = GenreID.from(anInput.id());
        final var aName = anInput.name();
        final var isActive = anInput.isActive();
        final var categories = toCategoryId(anInput.categories());

        final var aGenre = this.genreGateway.findByID(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(anInput.id()),
                    notification
            );
        }

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(List<CategoryID> ids) {
        final var notification = Notification.create();

        if (ids == null || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var commandsIds = new ArrayList<>(ids);
            commandsIds.removeAll(retrievedIds);

            final var missingIdsMessage = commandsIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(
                    new Error("Some categories could not be found: %s".formatted(missingIdsMessage)
                    ));


        }
        return notification;
    }

    private List<CategoryID> toCategoryId(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }

    public Supplier<NotFoundException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }
}
