package com.yansb.admin.api.application.video.update;

import com.yansb.admin.api.domain.Identifier;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.exceptions.InternalErrorException;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.handler.Notification;
import com.yansb.admin.api.domain.video.*;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {

  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;
  private final CastMemberGateway castMemberGateway;
  private final VideoGateway videoGateway;
  private final MediaResourceGateway mediaResourceGateway;

  public DefaultUpdateVideoUseCase(CategoryGateway categoryGateway, GenreGateway genreGateway, CastMemberGateway castMemberGateway, VideoGateway videoGateway, MediaResourceGateway mediaResourceGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
    this.genreGateway = Objects.requireNonNull(genreGateway);
    this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    this.videoGateway = Objects.requireNonNull(videoGateway);
    this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
  }

  @Override
  public UpdateVideoOutput execute(UpdateVideoCommand aCommand) {
    final var notification = Notification.create();
    final var anId = VideoID.from(aCommand.id());
    final var aRating = Rating.of(aCommand.rating())
        .orElse(null);
    final var aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
    final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
    final var genres = toIdentifier(aCommand.genres(), GenreID::from);
    final var members = toIdentifier(aCommand.members(), CastMemberID::from);

    final var aVideo = this.videoGateway.findById(anId)
        .orElseThrow(notFoundException(anId));

    notification.append(validateCategories(categories));
    notification.append(validateGenres(genres));
    notification.append(validateMembers(members));

    aVideo.update(
        aCommand.title(),
        aCommand.description(),
        aLaunchYear,
        aCommand.duration(),
        aCommand.opened(),
        aCommand.published(),
        aRating,
        categories,
        genres,
        members
    );

    aVideo.validate(notification);

    if (notification.hasError()) {
      throw new NotificationException("Could not update Aggregate Video", notification);
    }
    return UpdateVideoOutput.from(update(aCommand, aVideo));
  }

  private Video update(final UpdateVideoCommand aCommand, final Video aVideo) {
    final var anId = aVideo.getId();
    try {

      final var aVideoMedia = aCommand.getVideo()
          .map(it -> this.mediaResourceGateway.storeAudioVideo(anId, it))
          .orElse(null);

      final var aTrailerMedia = aCommand.getTrailer()
          .map(it -> this.mediaResourceGateway.storeAudioVideo(anId, it))
          .orElse(null);

      final var aBannerMedia = aCommand.getBanner()
          .map(it -> this.mediaResourceGateway.storeImage(anId, it))
          .orElse(null);

      final var aThumbnailMedia = aCommand.getThumbnail()
          .map(it -> this.mediaResourceGateway.storeImage(anId, it))
          .orElse(null);

      final var aThumbHalfMedia = aCommand.getThumbnailHalf()
          .map(it -> this.mediaResourceGateway.storeImage(anId, it))
          .orElse(null);

      return videoGateway.update(aVideo
          .setVideo(aVideoMedia)
          .setTrailer(aTrailerMedia)
          .setBanner(aBannerMedia)
          .setThumbnail(aThumbnailMedia)
          .setThumbnailHalf(aThumbHalfMedia));
    } catch (final Throwable t) {
      throw InternalErrorException.with("An error on updating video was observed [videoId:%s]"
          .formatted(anId.getValue()), t);
    }
  }

  private Supplier<DomainException> notFoundException(VideoID anId) {
    return () -> NotFoundException.with(Video.class, anId);
  }

  private ValidationHandler validateCategories(final Set<CategoryID> categories) {
    return validateAggregate("categories", categories, categoryGateway::existsByIds);
  }

  private ValidationHandler validateGenres(final Set<GenreID> genres) {
    return validateAggregate("genres", genres, genreGateway::existsByIds);
  }

  private ValidationHandler validateMembers(final Set<CastMemberID> members) {
    return validateAggregate("members", members, castMemberGateway::existsByIds);
  }

  private <T extends Identifier> ValidationHandler validateAggregate(
      final String aggregate,
      final Set<T> ids,
      final Function<Iterable<T>, List<T>> existsByIds
  ) {
    final var notification = Notification.create();
    if (ids == null || ids.isEmpty()) {
      return notification;
    }

    final var retrieveIds = existsByIds.apply(ids);

    if (ids.size() != retrieveIds.size()) {
      final var missingIds = new ArrayList<>(ids);
      missingIds.removeAll(retrieveIds);

      final var missingIdsMessage = missingIds.stream()
          .map(Identifier::getValue)
          .collect(Collectors.joining(", "));

      notification.append(new Error("Some %s could not be found %s".formatted(aggregate, missingIdsMessage)));
    }

    return notification;
  }

  private <T> Set<T> toIdentifier(final Set<String> values, final Function<String, T> mapper) {
    return values.stream()
        .map(mapper)
        .collect(Collectors.toSet());
  }
}
