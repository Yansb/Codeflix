package com.yansb.admin.api.domain.castMember;

import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.validation.ValidationHandler;
import com.yansb.admin.api.domain.validation.Validator;

public class CastMemberValidator extends Validator {
    private static final int NAME_MIN_LENGTH = 1;
    private static final int NAME_MAX_LENGTH = 255;
    private final CastMember castMember;

    protected CastMemberValidator(final CastMember aMember, final ValidationHandler aHandler) {
        super(aHandler);
        this.castMember = aMember;
    }

    @Override
    public void validate(){
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.castMember.getName();
        if(this.castMember.getName() == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
        }

        final var length = name.trim().length();
        if(length < NAME_MIN_LENGTH || length > NAME_MAX_LENGTH){
            this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
        }
    }

    private void checkTypeConstraints() {
        final var type = this.castMember.getType();
        if (type == null){
            this.validationHandler().append(new Error("'type' should not be null"));
        }
    }
}
