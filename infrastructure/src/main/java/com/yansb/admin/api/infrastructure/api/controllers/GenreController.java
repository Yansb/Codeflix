package com.yansb.admin.api.infrastructure.api.controllers;

import com.yansb.admin.api.application.genre.create.CreateGenreCommand;
import com.yansb.admin.api.application.genre.create.CreateGenreUseCase;
import com.yansb.admin.api.application.genre.delete.DeleteGenreUseCase;
import com.yansb.admin.api.application.genre.retrieve.get.GetGenreUseCase;
import com.yansb.admin.api.application.genre.retrieve.list.ListGenreUseCase;
import com.yansb.admin.api.application.genre.update.UpdateGenreCommand;
import com.yansb.admin.api.application.genre.update.UpdateGenreUseCase;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.api.GenreAPI;
import com.yansb.admin.api.infrastructure.genre.models.CreateGenreRequest;
import com.yansb.admin.api.infrastructure.genre.models.GenreListResponse;
import com.yansb.admin.api.infrastructure.genre.models.GenreResponse;
import com.yansb.admin.api.infrastructure.genre.models.UpdateGenreRequest;
import com.yansb.admin.api.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreUseCase getGenreUseCase;

    private final UpdateGenreUseCase updateGenreUseCase;

    private final DeleteGenreUseCase deleteGenreUseCase;

    private final ListGenreUseCase listGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreUseCase getGenreUseCase,
            final UpdateGenreUseCase updateGenreUseCase,
            DeleteGenreUseCase deleteGenreUseCase, ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreUseCase = getGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest  input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.active(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(final String search,final int page,final int perPage,final String sort,final  String dir) {
        return this.listGenreUseCase.execute(new SearchQuery(page, perPage, search, sort, dir))
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id,final UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.active(),
                input.categories()
        );

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}
