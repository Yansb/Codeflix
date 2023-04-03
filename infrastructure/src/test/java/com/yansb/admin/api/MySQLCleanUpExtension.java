package com.yansb.admin.api;

import com.yansb.admin.api.infrastructure.castMember.persistence.CastMemberRepository;
import com.yansb.admin.api.infrastructure.category.persistence.CategoryRepository;
import com.yansb.admin.api.infrastructure.genre.persistence.GenreRepository;
import com.yansb.admin.api.infrastructure.video.persistence.VideoRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(final ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
                appContext.getBean(VideoRepository.class),
                appContext.getBean(CastMemberRepository.class),
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}