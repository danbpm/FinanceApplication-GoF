package org.tigerbank.finance.service.export.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvDataServiceTest {

    @TempDir
    Path tempDir;

    private IBankAccountRepository accountRepo;
    private ICategoryRepository categoryRepo;
    private IOperationRepository operationRepo;
    private CsvDataService service;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        operationRepo = mock(IOperationRepository.class);
        service = new CsvDataService(accountRepo, categoryRepo, operationRepo);
    }

    @Test
    void exportToFile() throws IOException {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));

        when(accountRepo.findAll()).thenReturn(List.of(account));
        when(categoryRepo.findAll()).thenReturn(List.of());
        when(operationRepo.findAll()).thenReturn(List.of());

        String filePath = tempDir.toString() + "/";

        assertDoesNotThrow(() -> service.exportToFile(filePath));

        File accountsFile = new File(filePath + "accounts.csv");
        File categoriesFile = new File(filePath + "categories.csv");
        File operationsFile = new File(filePath + "operations.csv");

        assertTrue(accountsFile.exists());
        assertTrue(categoriesFile.exists());
        assertTrue(operationsFile.exists());
    }

    @Test
    void exportAllDataTypes() throws IOException {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("500.00"));
        Category category = Category.create("Зарплата", CategoryType.INCOME);
        Operation operation = Operation.create(OperationType.INCOME, account.getId(),
                new BigDecimal("100.00"), "Описание", category.getId());

        when(accountRepo.findAll()).thenReturn(List.of(account));
        when(categoryRepo.findAll()).thenReturn(List.of(category));
        when(operationRepo.findAll()).thenReturn(List.of(operation));

        String filePath = tempDir.toString() + "/";

        assertDoesNotThrow(() -> service.exportToFile(filePath));

        assertTrue(new File(filePath + "accounts.csv").exists());
        assertTrue(new File(filePath + "categories.csv").exists());
        assertTrue(new File(filePath + "operations.csv").exists());
    }

    @Test
    void exportCreatesDirectory() throws IOException {
        String filePath = tempDir.toString() + "/subdir/";

        when(accountRepo.findAll()).thenReturn(List.of());
        when(categoryRepo.findAll()).thenReturn(List.of());
        when(operationRepo.findAll()).thenReturn(List.of());

        assertDoesNotThrow(() -> service.exportToFile(filePath));

        assertTrue(new File(filePath).exists());
    }
}