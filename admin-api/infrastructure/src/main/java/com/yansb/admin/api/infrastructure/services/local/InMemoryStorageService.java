package com.yansb.admin.api.infrastructure.services.local;

import com.yansb.admin.api.domain.video.Resource;
import com.yansb.admin.api.infrastructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {
    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public Map<String, Resource> storage() {
        return storage;
    }

    public void reset() {
        this.storage.clear();
    }

    @Override
    public void deleteAll(Collection<String> names) {
        names.forEach(storage::remove);
    }

    @Override
    public Optional<Resource> get(String name) {
        return Optional.of(this.storage.get(name));
    }

    @Override
    public List<String> list(String prefix) {
        if (prefix == null) {
            return Collections.emptyList();
        }
        return this.storage.keySet().stream()
                .filter(it -> it.startsWith(prefix))
                .toList();
    }

    @Override
    public void store(String name, Resource resource) {
        this.storage.put(name, resource);
    }
}
