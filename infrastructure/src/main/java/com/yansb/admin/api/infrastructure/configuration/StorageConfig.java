package com.yansb.admin.api.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import com.yansb.admin.api.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.yansb.admin.api.infrastructure.configuration.properties.storage.StorageProperties;
import com.yansb.admin.api.infrastructure.services.StorageService;
import com.yansb.admin.api.infrastructure.services.impl.GCStorageService;
import com.yansb.admin.api.infrastructure.services.local.InMemoryStorageService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfig {

    @Bean
    @ConfigurationProperties(value = "storage.catalogo-videos")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean(name = "storageService")
    @Profile({
            "development",
            "production"
    })
    public StorageService gcStorageService(
            final GoogleStorageProperties props,
            final Storage storage
    ) {
        return new GCStorageService(props.getBucket(), storage);
    }

    @Bean(name = "storageService")
    @ConditionalOnMissingBean
    public InMemoryStorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
