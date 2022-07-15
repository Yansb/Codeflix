package com.yansb.admin.api.domain.validation.handler;

import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
  @Override
  public ValidationHandler append(Error anError) {
    throw DomainException.with(anError);
  }

  @Override
  public ValidationHandler append(final ValidationHandler anHandler) {
    throw DomainException.with(anHandler.getErrors());
  }

  @Override
  public ValidationHandler validate(Validation aValidation) {
    try {
      aValidation.validate();
    }catch (final Exception ex){
      throw DomainException.with(new Error(ex.getMessage()));
    }

    return this;
  }

  @Override
  public List<Error> getErrors() {
    return List.of();
  }

  @Override
  public boolean hasError() {
    return ValidationHandler.super.hasError();
  }
}
