package com.yansb.admin.api.domain.category;

public record CategorySearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction
) {
}
