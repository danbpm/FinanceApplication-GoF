package org.tigerbank.finance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тесты класса Operation")
class OperationTest {

    @Test
    @DisplayName("Создание операции с валидными данными")
    void createOperationWithValidData() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Operation operation = Operation.create(
                OperationType.INCOME,
                accountId,
                new BigDecimal("500.00"),
                "Зарплата",
                categoryId
        );

        assertNotNull(operation.getId());
        assertEquals(OperationType.INCOME, operation.getType());
        assertEquals(accountId, operation.getBankAccountId());
        assertEquals(new BigDecimal("500.00"), operation.getAmount());
        assertEquals("Зарплата", operation.getDescription());
        assertNotNull(operation.getDate());
        assertEquals(categoryId, operation.getCategoryId());
    }

    @Test
    @DisplayName("Создание операции с null типом")
    void createOperationWithNullType() {
        assertThrows(IllegalArgumentException.class, () ->
                Operation.create(null, UUID.randomUUID(), new BigDecimal("100.00"), "Описание", null)
        );
    }

    @Test
    @DisplayName("Создание операции с null ID счёта")
    void createOperationWithNullAccountId() {
        assertThrows(IllegalArgumentException.class, () ->
                Operation.create(OperationType.INCOME, null, new BigDecimal("100.00"), "Описание", null)
        );
    }

    @Test
    @DisplayName("Создание операции с null суммой")
    void createOperationWithNullAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                Operation.create(OperationType.INCOME, UUID.randomUUID(), null, "Описание", null)
        );
    }

    @Test
    @DisplayName("Создание операции с отрицательной суммой")
    void createOperationWithNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                Operation.create(OperationType.INCOME, UUID.randomUUID(),
                        new BigDecimal("-100.00"), "Описание", null)
        );
    }

    @Test
    @DisplayName("Создание операции с нулевой суммой")
    void createOperationWithZeroAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                Operation.create(OperationType.INCOME, UUID.randomUUID(),
                        BigDecimal.ZERO, "Описание", null)
        );
    }

    @Test
    @DisplayName("Создание операции с null описанием")
    void createOperationWithNullDescription() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                null,
                null
        );

        assertNull(operation.getDescription());
    }

    @Test
    @DisplayName("Создание операции с пустым описанием")
    void createOperationWithEmptyDescription() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                "   ",
                null
        );

        assertNull(operation.getDescription());
    }

    @Test
    @DisplayName("Создание операции с описанием > 255 символов")
    void createOperationWithLongDescription() {
        String longDesc = "a".repeat(256);
        assertThrows(IllegalArgumentException.class, () ->
                Operation.create(OperationType.INCOME, UUID.randomUUID(),
                        new BigDecimal("100.00"), longDesc, null)
        );
    }

    @Test
    @DisplayName("Обновление описания операции")
    void updateOperationDescription() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                "Старое описание",
                null
        );

        operation.updateDescription("Новое описание");
        assertEquals("Новое описание", operation.getDescription());
    }

    @Test
    @DisplayName("Привязка категории к операции")
    void assignCategoryToOperation() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                "Описание",
                null
        );

        Category category = Category.create("Зарплата", CategoryType.INCOME);
        operation.assignCategory(category);

        assertEquals(category.getId(), operation.getCategoryId());
    }

    @Test
    @DisplayName("Привязка несовместимой категории к операции")
    void assignIncompatibleCategoryToOperation() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                "Описание",
                null
        );

        Category category = Category.create("Продукты", CategoryType.EXPENSE);
        assertThrows(IllegalArgumentException.class, () ->
                operation.assignCategory(category)
        );
    }

    @Test
    @DisplayName("Отвязка категории от операции")
    void removeCategoryFromOperation() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                "Описание",
                UUID.randomUUID()
        );

        operation.removeCategory();
        assertNull(operation.getCategoryId());
    }

    @Test
    @DisplayName("Получение суммы со знаком для дохода")
    void getAmountWithSignForIncome() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("500.00"),
                "Описание",
                null
        );

        assertEquals(new BigDecimal("500.00"), operation.getAmountWithSign());
    }

    @Test
    @DisplayName("Получение суммы со знаком для расхода")
    void getAmountWithSignForExpense() {
        Operation operation = Operation.create(
                OperationType.EXPENSE,
                UUID.randomUUID(),
                new BigDecimal("500.00"),
                "Описание",
                null
        );

        assertEquals(new BigDecimal("-500.00"), operation.getAmountWithSign());
    }

    @Test
    @DisplayName("Дата операции устанавливается в текущую дату")
    void operationDateIsCurrentDate() {
        Operation operation = Operation.create(
                OperationType.INCOME,
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                "Описание",
                null
        );

        assertEquals(LocalDate.now(), operation.getDate());
    }
}