package com.yansb.admin.api.application.genre.create;

import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase{

  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;

  public DefaultCreateGenreUseCase(
      final CategoryGateway categoryGateway,
      final GenreGateway genreGateway
  ) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }

  @Override
  public CreateGenreOutput execute(CreateGenreCommand aCommand) {
    final var aName = aCommand.name();
    final var isActive = aCommand.isActive();
    final var categories = toCategoryID(aCommand.categories());

    final var notification = Notification.create();
    notification.append(validateCategories(categories));

    final var aGenre = notification.validate(() -> Genre.newGenre(
        aName,
        isActive
    ));

    if(notification.hasError()){
      throw new NotificationException("Could not create Aggregate genre", notification);
    }

    aGenre.addCategories(categories);


    return CreateGenreOutput.from(this.genreGateway.create(aGenre));
  }

  private ValidationHandler validateCategories(List<CategoryID> ids) {
    final var notification = Notification.create();

    if(ids == null || ids.isEmpty() ){
      return notification;
    }

    final var retrievedIds = categoryGateway.existsByIds(ids);

    if(ids.size() != retrievedIds.size()){
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

  private List<CategoryID> toCategoryID(final List<String> categories) {
    return categories.stream()
        .map(CategoryID::from)
        .toList();
  }
}
