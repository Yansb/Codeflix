package com.yansb.admin.api.infrastructure.configuration.usecases;

import com.yansb.admin.api.application.castMember.create.CreateCastMemberUseCase;
import com.yansb.admin.api.application.castMember.create.DefaultCreateCastMemberUseCase;
import com.yansb.admin.api.application.castMember.delete.DefaultDeleteCastMemberUseCase;
import com.yansb.admin.api.application.castMember.delete.DeleteCastMemberUseCase;
import com.yansb.admin.api.application.castMember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import com.yansb.admin.api.application.castMember.retrieve.get.GetCastMemberByIdUseCase;
import com.yansb.admin.api.application.castMember.retrieve.list.DefaultListCastMemberUseCase;
import com.yansb.admin.api.application.castMember.retrieve.list.ListCastMemberUseCase;
import com.yansb.admin.api.application.castMember.update.DefaultUpdateCastMemberUseCase;
import com.yansb.admin.api.application.castMember.update.UpdateCastMemberUseCase;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

  private final CastMemberGateway castMemberGateway;

  public CastMemberUseCaseConfig(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
  }

  @Bean
  public CreateCastMemberUseCase createCastMemberUseCase() {
    return new DefaultCreateCastMemberUseCase(castMemberGateway);
  }

  @Bean
  public DeleteCastMemberUseCase deleteCastMemberUseCase() {
    return new DefaultDeleteCastMemberUseCase(castMemberGateway);
  }

  @Bean
  public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
    return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
  }

  @Bean
  public ListCastMemberUseCase listCastMemberUseCase() {
    return new DefaultListCastMemberUseCase(castMemberGateway);
  }

  @Bean
  public UpdateCastMemberUseCase updateCastMemberUseCase() {
    return new DefaultUpdateCastMemberUseCase(castMemberGateway);
  }


}
