package com.yansb.admin.api.infrastructure.api.controllers;

import com.yansb.admin.api.application.video.create.CreateVideoCommand;
import com.yansb.admin.api.application.video.create.CreateVideoUseCase;
import com.yansb.admin.api.application.video.delete.DeleteVideoUseCase;
import com.yansb.admin.api.application.video.media.get.GetMediaCommand;
import com.yansb.admin.api.application.video.media.get.GetMediaUseCase;
import com.yansb.admin.api.application.video.media.upload.UploadMediaCommand;
import com.yansb.admin.api.application.video.media.upload.UploadMediaUseCase;
import com.yansb.admin.api.application.video.retrieve.get.GetVideoByIdUseCase;
import com.yansb.admin.api.application.video.retrieve.list.ListVideosUseCase;
import com.yansb.admin.api.application.video.update.UpdateVideoCommand;
import com.yansb.admin.api.application.video.update.UpdateVideoUseCase;
import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.exceptions.NotificationException;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.pagination.Pagination;
import com.yansb.admin.api.domain.utils.CollectionUtils;
import com.yansb.admin.api.domain.validation.Error;
import com.yansb.admin.api.domain.video.Resource;
import com.yansb.admin.api.domain.video.VideoMediaType;
import com.yansb.admin.api.domain.video.VideoResource;
import com.yansb.admin.api.domain.video.VideoSearchQuery;
import com.yansb.admin.api.infrastructure.api.VideoAPI;
import com.yansb.admin.api.infrastructure.utils.HashingUtils;
import com.yansb.admin.api.infrastructure.video.models.CreateVideoRequest;
import com.yansb.admin.api.infrastructure.video.models.UpdateVideoRequest;
import com.yansb.admin.api.infrastructure.video.models.VideoListResponse;
import com.yansb.admin.api.infrastructure.video.models.VideoResponse;
import com.yansb.admin.api.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {

    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final ListVideosUseCase listVideosUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final ListVideosUseCase listVideosUseCase,
            final GetMediaUseCase getMediaUseCase,
            final UploadMediaUseCase uploadMediaUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public Pagination<VideoListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String dir,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres
    ) {
        final var categoriesIds = CollectionUtils.mapTo(categories, CategoryID::from);
        final var genresIds = CollectionUtils.mapTo(genres, GenreID::from);
        final var castMembersIds = CollectionUtils.mapTo(castMembers, CastMemberID::from);

        final var aQuery = new VideoSearchQuery(
                page,
                perPage,
                search,
                sort,
                dir,
                categoriesIds,
                genresIds,
                castMembersIds
        );

        return VideoApiPresenter.present(this.listVideosUseCase.execute(aQuery));
    }

    @Override
    public ResponseEntity<?> createFull(
            final String title,
            final String description,
            final Integer yearLaunched,
            final Double duration,
            final Boolean opened,
            final Boolean published,
            final String rating,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final var aCmd = CreateVideoCommand.with(
                title,
                description,
                yearLaunched,
                duration,
                opened,
                published,
                rating,
                categories,
                genres,
                castMembers,
                resourceOf(videoFile),
                resourceOf(trailerFile),
                resourceOf(bannerFile),
                resourceOf(thumbFile),
                resourceOf(thumbHalfFile));

        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
        final var aCmd = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.createVideoUseCase.execute(aCmd);

        return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
    }

    @Override
    public VideoResponse getById(final String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateVideoRequest payload) {
        final var aCmd = UpdateVideoCommand.with(
                id,
                payload.title(),
                payload.description(),
                payload.yearLaunched(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final var output = this.updateVideoUseCase.execute(aCmd);

        return ResponseEntity.ok()
                .location(URI.create("/videos/" + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String id) {
        this.deleteVideoUseCase.execute(id);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(String id, String type) {
        final var aMedia =
                this.getMediaUseCase.execute(GetMediaCommand.with(id, type));

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=%s".formatted(aMedia.name())
                )
                .body(aMedia.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(final String id, final String type, final MultipartFile media) {
        final var aType = VideoMediaType.of(type)
                .orElseThrow(() -> NotificationException.with(new Error("Invalid %s for VideoMediaType".formatted(type))));
        final var aCmd = UploadMediaCommand.with(
                id,
                VideoResource.with(resourceOf(media), aType)
        );

        final var output = this.uploadMediaUseCase.execute(aCmd);
        return ResponseEntity.created(URI.create(("/videos/%s/medias/%s".formatted(id, type))))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }
        try {
            return Resource.with(
                    HashingUtils.checksum(part.getBytes()),
                    part.getBytes(),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
