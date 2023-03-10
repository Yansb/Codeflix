package com.yansb.admin.api.infrastructure.video.persistence;

import com.yansb.admin.api.domain.castMember.CastMemberID;
import com.yansb.admin.api.domain.category.CategoryID;
import com.yansb.admin.api.domain.genre.GenreID;
import com.yansb.admin.api.domain.video.VideoPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<VideoJpaEntity, String> {
    @Query("""
            select new com.yansb.admin.api.domain.video.VideoPreview(
                v.id,
                v.title,
                v.description,
                v.createdAt,
                v.updatedAt
            )
            from Video v
                join v.castMembers members
                join v.categories categories
                join v.genres genres
            where
                ( :terms is null or UPPER(v.title) like :terms)
            and
                ( :castMembers is null or members.id.castMemberId in :castMembers)
            and
                ( :categories is null or categories.id.categoryId in :categories)
            and
                ( :genres is null or genres.id.genreId in :genres)
            """)
    Page<VideoPreview> findAll(
            @Param("terms") final String terms,
            @Param("castMembers") final Set<String> castMembers,
            @Param("categories") final Set<String> categories,
            @Param("genres") final Set<String> genres,
            final Pageable page
    );
}
