package com.yansb.admin.api.application.castMember.retrieve.get;

import com.yansb.admin.api.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
    extends UseCase<String, CastMemberOutput>
    permits DefaultGetCastMemberByIdUseCase {
}
