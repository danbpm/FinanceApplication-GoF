package org.tigerbank.finance.facade;

import org.tigerbank.finance.model.Category;

import java.util.List;
import java.util.UUID;

public interface ICategoryFacade {
    public Category createCategory(Category category);
    public void deleteCategory(UUID id);
    public List<Category> getAllCategories();
}
