package com.yansb.admin.api.application.castMember.delete;

import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;

import java.util.Objects;

public non-sealed class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public DefaultDeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
    this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
  }


  @Override
  public void execute(String anInput) {
    this.castMemberGateway.deleteByID(CastMemberID.from(anInput));
  }
}
