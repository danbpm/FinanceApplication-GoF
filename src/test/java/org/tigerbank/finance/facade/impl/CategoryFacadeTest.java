
package org.tigerbank.finance.facade.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;
import org.tigerbank.finance.repository.ICategoryRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryFacadeTest {

    private ICategoryRepository categoryRepo;
    private CategoryFacade facade;

    @BeforeEach
    void setUp() {
        categoryRepo = mock(ICategoryRepository.class);
        facade = new CategoryFacade(categoryRepo);
    }

    @Test
    void createCategory() {
        Category category = Category.create("Зарплата", CategoryType.INCOME);

        when(categoryRepo.save(any())).thenReturn(category);

        Category result = facade.createCategory(category);

        assertNotNull(result);
        verify(categoryRepo).save(category);
    }

    @Test
    void deleteCategory() {
        UUID categoryId = UUID.randomUUID();

        facade.deleteCategory(categoryId);

        verify(categoryRepo).deleteById(categoryId);
    }

    @Test
    void getAllCategories() {
        when(categoryRepo.findAll()).thenReturn(List.of(
                Category.create("Категория 1", CategoryType.INCOME),
                Category.create("Категория 2", CategoryType.EXPENSE)
        ));

        List<Category> categories = facade.getAllCategories();

        assertEquals(2, categories.size());
    }
}