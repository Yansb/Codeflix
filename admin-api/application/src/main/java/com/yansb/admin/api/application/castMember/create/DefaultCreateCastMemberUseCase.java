package com.yansb.admin.api.application.castMember.create;

import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.validation.handler.Notification;

import java.util.Objects;

public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway aCastMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(aCastMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand anInput) {
        final var aName = anInput.name();
        final var aType = anInput.type();

        final var notification = Notification.create();

        final var aMember = notification.validate(() -> CastMember.newMember(aName, aType));

        if (notification.hasError()){
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private void notify(Notification notification) {
        throw new NotificationException("Could not create Aggregate CastMember", notification);
    }
}
