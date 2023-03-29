package com.yansb.admin.api.domain;

import com.yansb.admin.api.domain.event.DomainEvent;
import com.yansb.admin.api.domain.event.DomainEventPublisher;
import com.yansb.admin.api.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {
    protected final ID id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final ID id) {
        this(id, Collections.emptyList());
    }

    protected Entity(final ID id, final List<DomainEvent> domainEvents) {
        Objects.requireNonNull("'id' should not be null");
        this.id = id;
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
    }


    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher<DomainEvent> publisher) {
        if (publisher == null) {
            return;
        }
        getDomainEvents()
                .forEach(publisher::publishEvent);

        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
