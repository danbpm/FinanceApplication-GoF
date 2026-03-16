package org.tigerbank.finance.facade.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.service.IBalanceRecalculator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationFacadeTest {

    private IOperationRepository operationRepo;
    private ICategoryRepository categoryRepo;
    private IBankAccountRepository accountRepo;
    private IBalanceRecalculator recalculator;
    private OperationFacade facade;

    @BeforeEach
    void setUp() {
        operationRepo = mock(IOperationRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        accountRepo = mock(IBankAccountRepository.class);
        recalculator = mock(IBalanceRecalculator.class);
        facade = new OperationFacade(operationRepo, categoryRepo, accountRepo, recalculator);
    }

    @Test
    void createOperationWithoutCategory() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        Operation operation = Operation.create(OperationType.INCOME, accountId,
                new BigDecimal("500.00"), "Зарплата", null);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(operationRepo.save(any())).thenReturn(operation);

        Operation result = facade.createOperation(operation);

        assertNotNull(result);
        verify(operationRepo).save(operation);
        verify(recalculator).recalculateBalance(accountId);
    }

    @Test
    void createOperationWithCategory() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        Category category = Category.create("Зарплата", CategoryType.INCOME);
        Operation operation = Operation.create(OperationType.INCOME, accountId,
                new BigDecimal("500.00"), "Зарплата", categoryId);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(operationRepo.save(any())).thenReturn(operation);

        Operation result = facade.createOperation(operation);

        assertNotNull(result);
        verify(operationRepo).save(operation);
        verify(recalculator).recalculateBalance(accountId);
    }

    @Test
    void createOperationAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        Operation operation = Operation.create(OperationType.INCOME, accountId,
                new BigDecimal("500.00"), "Описание", null);

        when(accountRepo.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                facade.createOperation(operation)
        );
    }

    @Test
    void createOperationCategoryNotFound() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        Operation operation = Operation.create(OperationType.INCOME, accountId,
                new BigDecimal("500.00"), "Описание", categoryId);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                facade.createOperation(operation)
        );
    }

    @Test
    void deleteOperation() {
        UUID operationId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        Operation operation = Operation.create(OperationType.INCOME, accountId,
                new BigDecimal("500.00"), "Описание", null);

        when(operationRepo.findById(operationId)).thenReturn(Optional.of(operation));

        facade.deleteOperation(operationId);

        verify(operationRepo).deleteById(operationId);
        verify(recalculator).recalculateBalance(accountId);
    }

    @Test
    void deleteOperationNotFound() {
        UUID operationId = UUID.randomUUID();

        when(operationRepo.findById(operationId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                facade.deleteOperation(operationId)
        );
    }

    @Test
    void getByAccount() {
        UUID accountId = UUID.randomUUID();
        when(operationRepo.findByAccountId(accountId)).thenReturn(List.of(
                Operation.create(OperationType.INCOME, accountId, new BigDecimal("100.00"), "Описание 1", null)
        ));

        List<Operation> operations = facade.getByAccount(accountId);

        assertEquals(1, operations.size());
    }

    @Test
    void getByPeriod() {
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        when(operationRepo.findByPeriod(from, to)).thenReturn(List.of(
                Operation.create(OperationType.INCOME, UUID.randomUUID(), new BigDecimal("100.00"), "Описание", null)
        ));

        List<Operation> operations = facade.getByPeriod(from, to);

        assertEquals(1, operations.size());
    }
}