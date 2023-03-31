package com.yansb.admin.api.infrastructure.configuration.usecases;

import com.yansb.admin.api.application.video.create.CreateVideoUseCase;
import com.yansb.admin.api.application.video.create.DefaultCreateVideoUseCase;
import com.yansb.admin.api.application.video.delete.DefaultDeleteVideoUseCase;
import com.yansb.admin.api.application.video.delete.DeleteVideoUseCase;
import com.yansb.admin.api.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.yansb.admin.api.application.video.media.update.UpdateMediaStatusUseCase;
import com.yansb.admin.api.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.yansb.admin.api.application.video.retrieve.get.GetVideoByIdUseCase;
import com.yansb.admin.api.application.video.retrieve.list.DefaultListVideosUseCase;
import com.yansb.admin.api.application.video.retrieve.list.ListVideosUseCase;
import com.yansb.admin.api.application.video.update.DefaultUpdateVideoUseCase;
import com.yansb.admin.api.application.video.update.UpdateVideoUseCase;
import com.yansb.admin.api.domain.castMember.CastMemberGateway;
import com.yansb.admin.api.domain.category.CategoryGateway;
import com.yansb.admin.api.domain.genre.GenreGateway;
import com.yansb.admin.api.domain.video.MediaResourceGateway;
import com.yansb.admin.api.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {
    private final VideoGateway videoGateway;

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    private final CastMemberGateway castMemberGateway;

    private final MediaResourceGateway mediaResourceGateway;


    public VideoUseCaseConfig(
            VideoGateway videoGateway,
            CategoryGateway categoryGateway,
            GenreGateway genreGateway,
            CastMemberGateway castMemberGateway,
            MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, videoGateway, mediaResourceGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, videoGateway, mediaResourceGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }


}
