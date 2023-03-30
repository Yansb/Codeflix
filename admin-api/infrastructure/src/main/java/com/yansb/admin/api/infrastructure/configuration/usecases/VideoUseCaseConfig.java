package com.yansb.admin.api.infrastructure.configuration.usecases;

import com.yansb.admin.api.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.yansb.admin.api.application.video.media.update.UpdateMediaStatusUseCase;
import com.yansb.admin.api.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {
    private final VideoGateway videoGateway;


    public VideoUseCaseConfig(VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
