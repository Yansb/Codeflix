package com.yansb.admin.api.domain.castMember;

import com.yansb.admin.api.domain.AggregateRoot;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.utils.InstantUtils;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {
    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType){
        final var anId = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(anId,aName, aType,now ,now);
    }

    public static CastMember with(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate
    ){
        return new CastMember(anId,aName, aType,aCreationDate,aUpdateDate);
    }

    public static CastMember with(
            final CastMember aMember
    ){
        return new CastMember(
                aMember.getId(),
                aMember.getName(),
                aMember.getType(),
                aMember.getCreatedAt(),
                aMember.getUpdatedAt());
    }

    public CastMember update(final String aName, final CastMemberType aType){
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        new CastMemberValidator(this, aHandler).validate();
    }

    private void selfValidate(){
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()){
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }

    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
