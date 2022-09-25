package com.yansb.admin.api.domain.video;

import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.Validator;

public class VideoValidator extends Validator {

  private static final int TITLE_MAX_LENGTH = 255;
  private static final int TITLE_MIN_LENGTH = 1;

  private static final int DESCRIPTION_MAX_LENGTH = 4_000;

  private final Video video;

  protected VideoValidator(final Video aVideo, final ValidationHandler aHandler) {
    super(aHandler);
    this.video = aVideo;
  }

  @Override
  public void validate() {
    checkTitleConstraints();
    checkDescriptionConstraints();
    checkLaunchedAtConstraints();
    checkRatingConstraints();
  }

  private void checkTitleConstraints() {
    final var title = this.video.getTitle();
    if (this.video.getTitle() == null) {
      this.validationHandler().append(new Error("'title' should not be null"));
      return;
    }

    if (title.isBlank()) {
      this.validationHandler().append(new Error("'title' should not be empty"));
    }

    final var length = title.trim().length();
    if (length < TITLE_MIN_LENGTH || length > TITLE_MAX_LENGTH) {
      this.validationHandler().append(new Error("'title' must be between 1 and 255 characters"));
    }
  }

  private void checkDescriptionConstraints() {
    final var description = this.video.getDescription();

    if (description.isBlank()) {
      this.validationHandler().append(new Error("'description' should not be empty"));
    }

    final var length = description.trim().length();
    if (length > DESCRIPTION_MAX_LENGTH) {
      this.validationHandler().append(new Error("'description' must be between 1 and 255 characters"));
    }
  }

  private void checkLaunchedAtConstraints() {
    if (this.video.getLaunchedAt() == null) {
      this.validationHandler().append(new Error("'launchedAt' should not be null"));
    }
  }

  private void checkRatingConstraints() {
    if (this.video.getRating() == null) {
      this.validationHandler().append(new Error("'rating' should not be null"));
    }
  }
}

