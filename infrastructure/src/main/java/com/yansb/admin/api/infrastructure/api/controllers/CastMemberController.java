package com.yansb.admin.api.infrastructure.api.controllers;

import com.yansb.admin.api.application.castMember.create.CreateCastMemberCommand;
import com.yansb.admin.api.application.castMember.create.CreateCastMemberUseCase;
import com.yansb.admin.api.application.castMember.delete.DeleteCastMemberUseCase;
import com.yansb.admin.api.application.castMember.retrieve.get.GetCastMemberByIdUseCase;
import com.yansb.admin.api.application.castMember.retrieve.list.ListCastMemberUseCase;
import com.yansb.admin.api.application.castMember.update.UpdateCastMemberCommand;
import com.yansb.admin.api.application.castMember.update.UpdateCastMemberUseCase;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.pagination.SearchQuery;
import com.yansb.admin.api.infrastructure.api.CastMemberAPI;
import com.yansb.admin.api.infrastructure.castMember.models.CastMemberListResponse;
import com.yansb.admin.api.infrastructure.castMember.models.CastMemberResponse;
import com.yansb.admin.api.infrastructure.castMember.models.CreateCastMemberRequest;
import com.yansb.admin.api.infrastructure.castMember.models.UpdateCastMemberRequest;
import com.yansb.admin.api.infrastructure.castMember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
  private final CreateCastMemberUseCase createCastMemberUseCase;
  private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
  private final UpdateCastMemberUseCase updateCastMemberUseCase;
  private final DeleteCastMemberUseCase deleteCastMemberUseCase;
  private final ListCastMemberUseCase listCastMemberUseCase;

  public CastMemberController(
      final CreateCastMemberUseCase createCastMemberUseCase,
      final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
      final UpdateCastMemberUseCase updateCastMemberUseCase,
      final DeleteCastMemberUseCase deleteCastMemberUseCase,
      final ListCastMemberUseCase listCastMemberUseCase) {
    this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
    this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
    this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
    this.listCastMemberUseCase = Objects.requireNonNull(listCastMemberUseCase);
  }

  @Override
  public ResponseEntity<?> create(final CreateCastMemberRequest input) {
    final var aCommand =
        CreateCastMemberCommand.with(input.name(), input.type());
    final var output = this.createCastMemberUseCase.execute(aCommand);

    return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
  }

  @Override
  public CastMemberResponse getById(String id) {
    final var output = this.getCastMemberByIdUseCase.execute(id);
    return CastMemberPresenter.present(output);
  }

  @Override
  public Pagination<CastMemberListResponse> list(
      final String search,
      final int page,
      final int perPage,
      final String sort,
      final String dir) {
    return this.listCastMemberUseCase.execute(new SearchQuery(page, perPage, search, sort, dir))
        .map(CastMemberPresenter::present);
  }

  @Override
  public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
    final var aCommand =
        UpdateCastMemberCommand.with(id, aBody.name(), aBody.type());

    final var output = this.updateCastMemberUseCase.execute(aCommand);

    return ResponseEntity.ok(output);
  }

  @Override
  public void deleteById(final String id) {
    this.deleteCastMemberUseCase.execute(id);
  }
}
