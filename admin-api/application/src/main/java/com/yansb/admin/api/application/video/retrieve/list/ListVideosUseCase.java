package com.yansb.admin.api.application.video.retrieve.list;

import com.yansb.admin.api.application.UseCase;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
