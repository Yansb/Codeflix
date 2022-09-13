package com.yansb.admin.api.application.castMember.update;

import com.yansb.admin.api.domain.Identifier;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
    this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
  }

  private static void notify(final Identifier anId, Notification notification) {
    throw new NotificationException("Could not update Aggregate CastMember %s".formatted(anId.getValue()), notification);
  }

  @Override
  public UpdateCastMemberOutput execute(final UpdateCastMemberCommand anInput) {
    final var anId = CastMemberID.from(anInput.id());
    final var aName = anInput.name();
    final var aType = anInput.type();

    final var aMember = this.castMemberGateway.findByID(anId)
        .orElseThrow(notFound(anId));

    final var notification = Notification.create();

    notification.validate(() -> aMember.update(aName, aType));

    if (notification.hasError()) {
      notify(anId, notification);
    }

    return UpdateCastMemberOutput.from(this.castMemberGateway.update(aMember));
  }

  private Supplier<NotFoundException> notFound(final CastMemberID anId) {
    return () -> NotFoundException.with(CastMember.class, anId);
  }
}
