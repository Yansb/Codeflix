package com.yansb.admin.api.domain.category;

import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.Validator;

public class CategoryValidator extends Validator {
  public static final int NAME_MIN_LENGTH = 3;
  public static final int NAME_MAX_LENGTH = 255;
  private final Category category;

  protected CategoryValidator(final Category aCategory,final ValidationHandler aHandler) {
    super(aHandler);
    this.category = aCategory;
  }

  @Override
  public void validate() {
    checkNameConstraints();
  }

  private void checkNameConstraints() {
    final var name = this.category.getName();
    if(this.category.getName() == null){
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
    }

    if(name.isBlank()){
      this.validationHandler().append(new Error("'name' should not be empty"));
    }

    final var length = name.trim().length();
    if(length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH){
      this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
    }
  }
}
