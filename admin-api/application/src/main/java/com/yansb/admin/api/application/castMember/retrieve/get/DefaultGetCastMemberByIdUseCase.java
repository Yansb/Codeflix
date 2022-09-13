package com.yansb.admin.api.application.castMember.retrieve.get;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;

import java.util.Objects;

public non-sealed class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {

  private final CastMemberGateway castMemberGateway;

  public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
    this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
  }

  @Override
  public CastMemberOutput execute(String anInput) {
    final var aMemberId = CastMemberID.from(anInput);

    return this.castMemberGateway.findByID(aMemberId)
        .map(CastMemberOutput::from)
        .orElseThrow(() -> NotFoundException.with(CastMember.class, aMemberId));
  }
}
