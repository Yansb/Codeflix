package com.yansb.admin.api.domain.video;

public record VideoSearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction
) {
}
