package com.yansb.admin.api.domain.event;

@FunctionalInterface
public interface DomainEventPublisher<T extends DomainEvent> {
    void publishEvent(T event);
}
