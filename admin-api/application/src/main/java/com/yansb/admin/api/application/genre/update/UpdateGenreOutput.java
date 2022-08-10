package com.yansb.admin.api.application.genre.update;

import com.yansb.admin.api.domain.genre.Genre;

public record UpdateGenreOutput(String id) {

  public static UpdateGenreOutput from(final Genre aGenre) {
    return new UpdateGenreOutput(aGenre.getId().getValue());
  }
}
