package com.yansb.admin.api.application.castMember.create;

import com.yansb.admin.api.application.UseCase;

public sealed abstract class CreateCastMemberUseCase extends UseCase<CreateCastMemberCommand,CreateCastMemberOutput>
permits DefaultCreateCastMemberUseCase {
}
