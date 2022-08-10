package com.yansb.admin.api.application.genre.update;

import com.yansb.admin.api.domain.genre.GenreID;

import java.util.List;

public record UpdateGenreCommand(
    GenreID id,
    String name,
    boolean isActive,
    List<String> categories
) {
    public static UpdateGenreCommand with(
        final GenreID id,
        final String name,
        final Boolean isActive,
        final List<String> categories
    ) {
        return new UpdateGenreCommand(id, name, isActive != null ? isActive : true, categories);
    }
}
