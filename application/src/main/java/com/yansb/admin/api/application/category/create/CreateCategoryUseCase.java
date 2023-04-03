package com.yansb.admin.api.application.category.create;

import com.yansb.admin.api.application.UseCase;
import com.yansb.admin.api.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
    extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {


}
