
package org.tigerbank.finance.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCategoryRepositoryTest {

    private InMemoryCategoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryCategoryRepository();
    }

    @Test
    void saveCategory() {
        Category category = Category.create("Зарплата", CategoryType.INCOME);

        Category saved = repository.save(category);

        assertNotNull(saved);
        assertEquals(category.getId(), saved.getId());
    }

    @Test
    void findByIdExisting() {
        Category category = Category.create("Продукты", CategoryType.EXPENSE);
        repository.save(category);

        Optional<Category> found = repository.findById(category.getId());

        assertTrue(found.isPresent());
    }

    @Test
    void findByIdNotExisting() {
        Optional<Category> found = repository.findById(UUID.randomUUID());

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll() {
        repository.save(Category.create("Категория 1", CategoryType.INCOME));
        repository.save(Category.create("Категория 2", CategoryType.EXPENSE));

        List<Category> categories = repository.findAll();

        assertEquals(2, categories.size());
    }

    @Test
    void deleteById() {
        Category category = Category.create("Тест", CategoryType.INCOME);
        repository.save(category);

        repository.deleteById(category.getId());

        Optional<Category> found = repository.findById(category.getId());
        assertTrue(found.isEmpty());
    }
}