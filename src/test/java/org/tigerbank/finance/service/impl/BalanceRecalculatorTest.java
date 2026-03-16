package org.tigerbank.finance.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.IOperationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BalanceRecalculatorTest {

    private IBankAccountRepository accountRepo;
    private IOperationRepository operationRepo;
    private BalanceRecalculator recalculator;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        operationRepo = mock(IOperationRepository.class);
        recalculator = new BalanceRecalculator(accountRepo, operationRepo);
    }

    @Test
    void recalculateBalanceWithIncomeOperations() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));

        Operation op1 = Operation.create(OperationType.INCOME, accountId, new BigDecimal("500.00"), "Доход 1", null);
        Operation op2 = Operation.create(OperationType.INCOME, accountId, new BigDecimal("300.00"), "Доход 2", null);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(operationRepo.findByAccountId(accountId)).thenReturn(List.of(op1, op2));

        recalculator.recalculateBalance(accountId);

        verify(accountRepo).save(account);

        assertEquals(new BigDecimal("800.00"), account.getBalance());
    }

    @Test
    void recalculateBalanceWithExpenseOperations() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));

        Operation startOp = Operation.create(OperationType.INCOME, accountId, account.getBalance(), "Стартовое зачисление", null);
        Operation op1 = Operation.create(OperationType.EXPENSE, accountId, new BigDecimal("200.00"), "Расход 1", null);
        Operation op2 = Operation.create(OperationType.EXPENSE, accountId, new BigDecimal("300.00"), "Расход 2", null);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(operationRepo.findByAccountId(accountId)).thenReturn(List.of(startOp, op1, op2));

        recalculator.recalculateBalance(accountId);

        verify(accountRepo).save(account);
        assertEquals(new BigDecimal("500.00"), account.getBalance());
    }

    @Test
    void recalculateBalanceWithMixedOperations() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("5000.00"));

        Operation income = Operation.create(OperationType.INCOME, accountId, new BigDecimal("1000.00"), "Доход", null);
        Operation expense = Operation.create(OperationType.EXPENSE, accountId, new BigDecimal("400.00"), "Расход", null);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));
        when(operationRepo.findByAccountId(accountId)).thenReturn(List.of(income, expense));

        recalculator.recalculateBalance(accountId);

        // Баланс: 1000 - 400 = 600 (начальные 5000 игнорируются!)
        assertEquals(new BigDecimal("600.00"), account.getBalance());
    }

    @Test
    void recalculateBalanceAccountNotFound() {
        UUID accountId = UUID.randomUUID();

        when(accountRepo.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                recalculator.recalculateBalance(accountId)
        );
    }
}