package com.yansb.admin.api.application.category.update;

import com.yansb.admin.api.application.UseCase;
import com.yansb.admin.api.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
    extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
