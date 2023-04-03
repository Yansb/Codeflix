package com.yansb.admin.api.application;

public abstract class UnitUseCase<IN> {
  public abstract void execute(IN anInput);
}
