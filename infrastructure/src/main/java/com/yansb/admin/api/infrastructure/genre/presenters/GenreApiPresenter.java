package com.yansb.admin.api.infrastructure.genre.presenters;

import com.yansb.admin.api.application.genre.retrieve.get.GenreOutput;
import com.yansb.admin.api.application.genre.retrieve.list.GenreListOutput;
import com.yansb.admin.api.infrastructure.genre.models.GenreListResponse;
import com.yansb.admin.api.infrastructure.genre.models.GenreResponse;

public interface GenreApiPresenter {
    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id().getValue(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
