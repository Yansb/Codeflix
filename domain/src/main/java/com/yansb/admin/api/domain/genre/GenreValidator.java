package com.yansb.admin.api.domain.genre;

import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.Validator;

public class GenreValidator extends Validator {

  private static final int NAME_MIN_LENGTH = 1;
  private static final int NAME_MAX_LENGTH = 255;

  private final Genre genre;

  protected GenreValidator(final Genre aGenre, final ValidationHandler aHandler){
    super(aHandler);
    this.genre = aGenre;
  }

  @Override
  public void validate() {
    checkNameConstraints();
  }

  private void checkNameConstraints() {
    final var name = this.genre.getName();
    if(this.genre.getName() == null){
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
    }

    if(name.isBlank()){
      this.validationHandler().append(new Error("'name' should not be empty"));
    }

    final var length = name.trim().length();
    if(length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH){
      this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
    }
  }
}
