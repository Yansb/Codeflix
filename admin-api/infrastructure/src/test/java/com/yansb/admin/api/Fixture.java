package com.yansb.admin.api;

import com.github.javafaker.Faker;
import com.yansb.admin.api.domain.castMember.CastMemberType;

public final class Fixture {
  public static final Faker FAKER = new Faker();

  public static String name() {
    return FAKER.name().fullName();
  }

  public static final class CastMember {
    public static CastMemberType type() {
      return FAKER.options().option(CastMemberType.ACTOR, CastMemberType.DIRECTOR);
    }
  }
}
