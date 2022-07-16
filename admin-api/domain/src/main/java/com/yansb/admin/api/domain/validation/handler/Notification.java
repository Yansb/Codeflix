package com.yansb.admin.api.domain.validation.handler;

import com.yansb.admin.api.domain.exceptions.DomainException;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

  private final List<Error> errors;

  private Notification(List<Error> errors) {
    this.errors = errors;
  }

  public static Notification create(){
    return new Notification(new ArrayList<>());
  }

  public static Notification create(final Error anError){
    return new Notification(new ArrayList<>()).append(anError);
  }

  public static Notification create(final Throwable t){
    return create(new Error(t.getMessage()));
  }
  @Override
  public Notification append(Error anError) {
    this.errors.add(anError);
    return this;
  }

  @Override
  public Notification append(ValidationHandler anHandler) {
    this.errors.addAll(anHandler.getErrors());
    return this;
  }

  @Override
  public Notification validate(final Validation aValidation) {
    try {
      aValidation.validate();
    }catch (final DomainException ex){
      this.errors.addAll(ex.getErrors());
    }catch (final Throwable t){
      this.errors.add(new Error(t.getMessage()));
    }
    return this;
  }

  @Override
  public List<Error> getErrors() {
    return this.errors;
  }

  @Override
  public boolean hasError() {
    return ValidationHandler.super.hasError();
  }
}
