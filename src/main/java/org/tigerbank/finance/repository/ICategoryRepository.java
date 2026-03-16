package org.tigerbank.finance.repository;

import org.tigerbank.finance.model.Category;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface ICategoryRepository {
    Category save(Category category);
    Optional<Category> findById(UUID id);
    List<Category> findAll();
    void deleteById(UUID id);
}
