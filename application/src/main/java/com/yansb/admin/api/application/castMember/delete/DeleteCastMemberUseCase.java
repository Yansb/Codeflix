package com.yansb.admin.api.application.castMember.delete;

import com.yansb.admin.api.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
    extends UnitUseCase<String>
    permits DefaultDeleteCastMemberUseCase {
}
