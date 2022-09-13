package com.yansb.admin.api.infrastructure.api.controllers;

import com.yansb.admin.api.application.castMember.create.CreateCastMemberCommand;
import com.yansb.admin.api.application.castMember.create.CreateCastMemberUseCase;
import com.yansb.admin.api.application.castMember.retrieve.get.GetCastMemberByIdUseCase;
import com.yansb.admin.api.infrastructure.api.CastMemberAPI;
import com.yansb.admin.api.infrastructure.castMember.models.CastMemberResponse;
import com.yansb.admin.api.infrastructure.castMember.models.CreateCastMemberRequest;
import com.yansb.admin.api.infrastructure.castMember.presenter.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {
  private final CreateCastMemberUseCase createCastMemberUseCase;
  private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;

  public CastMemberController(CreateCastMemberUseCase createCastMemberUseCase, GetCastMemberByIdUseCase getCastMemberByIdUseCase) {
    this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
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
}
