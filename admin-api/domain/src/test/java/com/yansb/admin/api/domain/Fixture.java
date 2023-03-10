package com.yansb.admin.api.domain;

import com.github.javafaker.Faker;
import com.yansb.admin.api.domain.castMember.CastMember;
import com.yansb.admin.api.domain.castMember.CastMemberType;
import com.yansb.admin.api.domain.category.Category;
import com.yansb.admin.api.domain.genre.Genre;
import com.yansb.admin.api.domain.video.Rating;
import com.yansb.admin.api.domain.video.Resource;
import com.yansb.admin.api.domain.video.Video;
import com.yansb.admin.api.domain.video.VideoPreview;

import java.time.Instant;
import java.time.Year;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static io.vavr.API.*;

public final class Fixture {
  public static final Faker FAKER = new Faker();

  public static String name() {
    return FAKER.name().fullName();
  }

  public static String id(){
    return FAKER.idNumber().valid();
  }

  public static String title() {
    return FAKER.book().title();
  }

  public static Integer year() {
    return FAKER.number().numberBetween(1900, 2050);
  }

  public static boolean bool() {
    return FAKER.bool().bool();
  }

  public static Video aVideo() {
    return Video.newVideo(
        Fixture.title(),
        Videos.description(),
        Year.of(Fixture.year()),
        Videos.duration(),
        Fixture.bool(),
        Fixture.bool(),
        Videos.rating(),
        Set.of(Categories.documentaries().getId()),
        Set.of(Genres.action().getId()),
        Set.of(CastMembers.yan().getId(), CastMembers.gabriel().getId())
    );
  }

  public static VideoPreview aVideoPreview(){
    return new VideoPreview(
            Fixture.id(),
            Fixture.title(),
            Videos.description(),
            Instant.from(FAKER.date().past(
                    100,
                    TimeUnit.DAYS
            ).toInstant(
            )),
            Instant.from(FAKER.date().past(
                    100,
                    TimeUnit.DAYS
            ).toInstant(
            ))
    );
  }

  public static final class Categories {
    private static final Category DOCUMENTARIES = Category.newCategory("Documentaries", "some description", true);

    public static Category documentaries() {
      return DOCUMENTARIES.clone();
    }
  }

  public static final class Genres {
    private static final Genre ACTION = Genre.newGenre("Action", true);

    public static Genre action() {
      return Genre.with(ACTION);
    }

  }

  public static final class CastMembers {

    private static final CastMember YAN = CastMember.newMember("Yan Santana", CastMemberType.ACTOR);
    private static final CastMember GABRIEL = CastMember.newMember("Gabriel Suaki", CastMemberType.ACTOR);

    public static CastMemberType type() {
      return FAKER.options().option(CastMemberType.values());
    }

    public static CastMember yan() {
      return CastMember.with(YAN);
    }

    public static CastMember gabriel() {
      return CastMember.with(GABRIEL);
    }
  }

  public static final class Videos {
    public static Video lordOfTheRings() {
      return Video.newVideo(
          "Lord of the Rings",
          description(),
          Year.of(Fixture.year()),
          duration(),
          Fixture.bool(),
          Fixture.bool(),
          rating(),
          Set.of(Categories.documentaries().getId()),
          Set.of(Genres.action().getId()),
          Set.of(CastMembers.yan().getId(), CastMembers.gabriel().getId())
      );
    }

    public static String description() {
      return FAKER.lorem().paragraph();
    }

    public static Resource resource(final Resource.Type type) {
      final String contentType = Match(type).of(
          Case($(List(Resource.Type.VIDEO, Resource.Type.TRAILER)::contains), "video/mp4"),
          Case($(), "image/jpg")
      );

      final byte[] content = FAKER.internet().image().getBytes();
      return Resource.with(content, contentType, type.name().toLowerCase(), type);
    }

    public static Rating rating() {
      return FAKER.options().option(Rating.values());
    }

    public static Double duration() {
      return FAKER.number().randomDouble(3, 1, 300);
    }
  }
}
