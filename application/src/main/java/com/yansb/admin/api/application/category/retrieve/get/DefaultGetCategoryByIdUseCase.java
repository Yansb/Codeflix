package com.yansb.admin.api.application.category.retrieve.get;

import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anInput) {
        final var anCategoryId = CategoryID.from(anInput);

        return this.categoryGateway.findById(anCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(anCategoryId));
    }


    public Supplier<NotFoundException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
