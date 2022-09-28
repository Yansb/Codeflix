package com.yansb.admin.api.application.video.create;

import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.video.Rating;

import java.util.function.Supplier;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {


  @Override
  public CreateVideoOutput execute(final CreateVideoCommand aCommand) {
    final var aRating = Rating.of(aCommand.rating())
        .orElseThrow(invalidRating(aCommand.rating()));


    return null;
  }

  private Supplier<DomainException> invalidRating(final String rating) {
    return () -> DomainException.with(new Error("Rating not found %s".formatted(rating)));
  }
}
