package com.yansb.admin.api.application.castMember.update;

import com.yansb.admin.api.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput> permits DefaultUpdateCastMemberUseCase {
}

