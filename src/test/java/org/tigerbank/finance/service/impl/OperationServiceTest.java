package org.tigerbank.finance.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.service.IBalanceRecalculator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationServiceTest {

    private IBankAccountRepository accountRepo;
    private IOperationRepository operationRepo;
    private ICategoryRepository categoryRepo;
    private IBalanceRecalculator recalculator;
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        operationRepo = mock(IOperationRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        recalculator = mock(IBalanceRecalculator.class);
        operationService = new OperationService(accountRepo, operationRepo, categoryRepo, recalculator);
    }

    @Test
    void addOperationWithoutCategory() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        Operation operation = Operation.create(OperationType.INCOME, accountId, new BigDecimal("500.00"), "Зарплата", null);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(operationRepo.save(any(Operation.class))).thenReturn(operation);

        Operation saved = operationService.addOperation(operation);

        assertNotNull(saved);
        verify(operationRepo).save(operation);
        verify(recalculator).recalculateBalance(accountId);
    }

    @Test
    void addOperationWithCategory() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        Category category = Category.create("Зарплата", CategoryType.INCOME);
        Operation operation = Operation.create(OperationType.INCOME, accountId, new BigDecimal("500.00"), "Зарплата", categoryId);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));
        when(operationRepo.save(any(Operation.class))).thenReturn(operation);

        Operation saved = operationService.addOperation(operation);

        assertNotNull(saved);
        verify(operationRepo).save(operation);
        verify(recalculator).recalculateBalance(accountId);
    }

    @Test
    void addOperationAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        Operation operation = Operation.create(OperationType.INCOME, accountId, new BigDecimal("500.00"), "Описание", null);

        when(accountRepo.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                operationService.addOperation(operation)
        );
    }

    @Test
    void addOperationCategoryNotFound() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));
        Operation operation = Operation.create(OperationType.INCOME, accountId, new BigDecimal("500.00"), "Описание", categoryId);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                operationService.addOperation(operation)
        );
    }

    @Test
    void deleteOperation() {
        UUID operationId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        Operation operation = Operation.create(OperationType.INCOME, accountId, new BigDecimal("500.00"), "Описание", null);

        when(operationRepo.findById(operationId)).thenReturn(Optional.of(operation));

        operationService.deleteOperation(operationId);

        verify(operationRepo).deleteById(operationId);
        verify(recalculator).recalculateBalance(accountId);
    }

    @Test
    void deleteOperationNotFound() {
        UUID operationId = UUID.randomUUID();

        when(operationRepo.findById(operationId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                operationService.deleteOperation(operationId)
        );
    }

    @Test
    void getAccountOperations() {
        UUID accountId = UUID.randomUUID();
        Operation op1 = Operation.create(OperationType.INCOME, accountId, new BigDecimal("100.00"), "Описание 1", null);
        Operation op2 = Operation.create(OperationType.INCOME, accountId, new BigDecimal("200.00"), "Описание 2", null);

        when(operationRepo.findByAccountId(accountId)).thenReturn(List.of(op1, op2));

        List<Operation> operations = operationService.getAccountOperations(accountId);

        assertEquals(2, operations.size());
    }

    @Test
    void getAccountBalance() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1500.00"));

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));

        BigDecimal balance = operationService.getAccountBalance(accountId);

        assertEquals(new BigDecimal("1500.00"), balance);
    }

    @Test
    void getAccountBalanceNotFound() {
        UUID accountId = UUID.randomUUID();

        when(accountRepo.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                operationService.getAccountBalance(accountId)
        );
    }
}