package org.tigerbank.finance.repository.impl;

import org.tigerbank.finance.model.Category;

import org.springframework.stereotype.Repository;
import org.tigerbank.finance.repository.ICategoryRepository;

import java.util.UUID;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;

@Repository
public class InMemoryCategoryRepository implements ICategoryRepository {
    private final Map<UUID, Category> storage = new ConcurrentHashMap<>();

    @Override
    public Category save(Category category) {
        storage.put(category.getId(), category);
        return category;
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
