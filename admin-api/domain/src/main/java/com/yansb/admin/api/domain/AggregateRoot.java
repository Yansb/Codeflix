package com.yansb.admin.api.domain;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID>{

  protected AggregateRoot(ID id) {
    super(id);
  }
}
