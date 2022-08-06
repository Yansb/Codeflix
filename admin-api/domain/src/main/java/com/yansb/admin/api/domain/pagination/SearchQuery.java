package com.yansb.admin.api.domain.pagination;

public record SearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction
) {
}
