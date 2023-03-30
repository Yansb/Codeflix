package com.yansb.admin.api.domain.video;

import com.yansb.admin.api.domain.event.DomainEvent;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {
    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, Instant.now());
    }
}
