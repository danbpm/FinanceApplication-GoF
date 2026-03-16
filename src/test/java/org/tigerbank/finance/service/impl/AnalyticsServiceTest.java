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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

    private IOperationRepository operationRepo;
    private IBankAccountRepository accountRepo;
    private ICategoryRepository categoryRepo;
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        operationRepo = mock(IOperationRepository.class);
        accountRepo = mock(IBankAccountRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        analyticsService = new AnalyticsService(operationRepo, categoryRepo, accountRepo);
    }

    @Test
    void calculateNetIncome() {
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        Operation income1 = Operation.create(OperationType.INCOME, UUID.randomUUID(), new BigDecimal("1000.00"), "Доход 1", null);
        Operation income2 = Operation.create(OperationType.INCOME, UUID.randomUUID(), new BigDecimal("500.00"), "Доход 2", null);
        Operation expense1 = Operation.create(OperationType.EXPENSE, UUID.randomUUID(), new BigDecimal("300.00"), "Расход 1", null);

        when(operationRepo.findByPeriod(from, to)).thenReturn(List.of(income1, income2, expense1));

        BigDecimal netIncome = analyticsService.calculateNetIncome(from, to);

        assertEquals(new BigDecimal("1200.00"), netIncome);
    }

    @Test
    void calculateNetIncomeNoOperations() {
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        when(operationRepo.findByPeriod(from, to)).thenReturn(List.of());

        BigDecimal netIncome = analyticsService.calculateNetIncome(from, to);

        assertEquals(BigDecimal.ZERO, netIncome);
    }

    @Test
    void calculateNetIncomeByAccount() {
        UUID accountId = UUID.randomUUID();
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        Operation income1 = Operation.create(OperationType.INCOME, accountId, new BigDecimal("1000.00"), "Доход 1", null);
        Operation expense1 = Operation.create(OperationType.EXPENSE, accountId, new BigDecimal("400.00"), "Расход 1", null);

        when(operationRepo.findByAccountAndPeriod(accountId, from, to)).thenReturn(List.of(income1, expense1));

        BigDecimal netIncome = analyticsService.calculateNetIncomeByAccount(accountId, from, to);

        assertEquals(new BigDecimal("600.00"), netIncome);
    }

    @Test
    void groupByCategory() {
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();
        UUID categoryId = UUID.randomUUID();
        Category category = Category.create("Зарплата", CategoryType.INCOME);

        Operation op1 = Operation.create(OperationType.INCOME, UUID.randomUUID(), new BigDecimal("500.00"), "Описание 1", categoryId);
        Operation op2 = Operation.create(OperationType.INCOME, UUID.randomUUID(), new BigDecimal("300.00"), "Описание 2", categoryId);

        when(operationRepo.findByPeriod(from, to)).thenReturn(List.of(op1, op2));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        Map<Category, BigDecimal> result = analyticsService.groupByCategory(from, to, OperationType.INCOME);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("800.00"), result.get(category));
    }

    @Test
    void groupByCategoryWithAccount() {
        UUID accountId = UUID.randomUUID();
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();
        UUID categoryId = UUID.randomUUID();
        Category category = Category.create("Продукты", CategoryType.EXPENSE);

        Operation op1 = Operation.create(OperationType.EXPENSE, accountId, new BigDecimal("200.00"), "Описание 1", categoryId);

        when(operationRepo.findByAccountAndPeriod(accountId, from, to)).thenReturn(List.of(op1));
        when(categoryRepo.findById(categoryId)).thenReturn(Optional.of(category));

        Map<Category, BigDecimal> result = analyticsService.groupByCategory(accountId, from, to, OperationType.EXPENSE);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("200.00"), result.get(category));
    }

    @Test
    void getStatisticsByAccountNotFound() {
        UUID accountId = UUID.randomUUID();
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        when(accountRepo.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                analyticsService.getStatisticsByAccount(accountId, from, to)
        );
    }
}