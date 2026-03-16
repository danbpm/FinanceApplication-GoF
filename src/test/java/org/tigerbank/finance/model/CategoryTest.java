package org.tigerbank.finance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса Category")
class CategoryTest {

    @Test
    @DisplayName("Создание категории с валидными данными")
    void createCategoryWithValidData() {
        Category category = Category.create("Зарплата", CategoryType.INCOME);

        assertNotNull(category.getId());
        assertEquals("Зарплата", category.getName());
        assertEquals(CategoryType.INCOME, category.getType());
    }

    @Test
    @DisplayName("Создание категории с null типом")
    void createCategoryWithNullType() {
        assertThrows(IllegalArgumentException.class, () ->
                Category.create("Категория", null)
        );
    }

    @Test
    @DisplayName("Создание категории с null именем")
    void createCategoryWithNullName() {
        assertThrows(IllegalArgumentException.class, () ->
                Category.create(null, CategoryType.INCOME)
        );
    }

    @Test
    @DisplayName("Создание категории с пустым именем")
    void createCategoryWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () ->
                Category.create("   ", CategoryType.INCOME)
        );
    }

    @Test
    @DisplayName("Создание категории с именем > 50 символов")
    void createCategoryWithLongName() {
        String longName = "a".repeat(51);
        assertThrows(IllegalArgumentException.class, () ->
                Category.create(longName, CategoryType.INCOME)
        );
    }

    @Test
    @DisplayName("Переименование категории")
    void renameCategory() {
        Category category = Category.create("Старое имя", CategoryType.INCOME);
        category.rename("Новое имя");

        assertEquals("Новое имя", category.getName());
    }

    @Test
    @DisplayName("Переименование категории с пробелами")
    void renameCategoryWithSpaces() {
        Category category = Category.create("Имя", CategoryType.INCOME);
        category.rename("  Новое имя  ");

        assertEquals("Новое имя", category.getName());
    }

    @Test
    @DisplayName("Валидация совместимости Income категории с Income операцией")
    void validateIncomeCategoryWithIncomeOperation() {
        Category category = Category.create("Зарплата", CategoryType.INCOME);
        // Не должно выбрасывать исключение
        assertDoesNotThrow(() ->
                category.validateOperationCompatibility(OperationType.INCOME)
        );
    }

    @Test
    @DisplayName("Валидация совместимости Expense категории с Expense операцией")
    void validateExpenseCategoryWithExpenseOperation() {
        Category category = Category.create("Продукты", CategoryType.EXPENSE);
        // Не должно выбрасывать исключение
        assertDoesNotThrow(() ->
                category.validateOperationCompatibility(OperationType.EXPENSE)
        );
    }

    @Test
    @DisplayName("Валидация несовместимости Income категории с Expense операцией")
    void validateIncomeCategoryWithExpenseOperation() {
        Category category = Category.create("Зарплата", CategoryType.INCOME);
        assertThrows(IllegalArgumentException.class, () ->
                category.validateOperationCompatibility(OperationType.EXPENSE)
        );
    }

    @Test
    @DisplayName("Валидация несовместимости Expense категории с Income операцией")
    void validateExpenseCategoryWithIncomeOperation() {
        Category category = Category.create("Продукты", CategoryType.EXPENSE);
        assertThrows(IllegalArgumentException.class, () ->
                category.validateOperationCompatibility(OperationType.INCOME)
        );
    }

    @Test
    @DisplayName("toString возвращает корректное представление")
    void toStringReturnsCorrectFormat() {
        Category category = Category.create("Тест", CategoryType.INCOME);
        String result = category.toString();

        assertTrue(result.contains("Category"));
        assertTrue(result.contains("id="));
        assertTrue(result.contains("type="));
        assertTrue(result.contains("name=Тест"));
    }
}