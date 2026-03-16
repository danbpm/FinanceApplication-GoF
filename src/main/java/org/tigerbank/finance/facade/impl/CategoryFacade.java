package org.tigerbank.finance.facade.impl;

import org.springframework.stereotype.Component;
import org.tigerbank.finance.facade.ICategoryFacade;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.repository.ICategoryRepository;

import java.util.List;
import java.util.UUID;

@Component
public class CategoryFacade implements ICategoryFacade {
    private final ICategoryRepository categoryRepo;

    public CategoryFacade(ICategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }
}