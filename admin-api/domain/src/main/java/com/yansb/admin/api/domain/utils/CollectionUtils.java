package com.yansb.admin.api.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {
  private CollectionUtils() {
  }

  public static <IN, OUT> Set<OUT> mapTo(final Set<IN> list, Function<IN, OUT> mapper) {
    if(list == null || list.isEmpty()) {
      return null;
    }

    return list.stream()
        .map(mapper)
        .collect(Collectors.toSet());
  }

  public static <T> Set<T> nullIfEmpty(final Set<T> values) {
    if(values == null || values.isEmpty()) {
      return null;
    }
    return values;
  }
}
