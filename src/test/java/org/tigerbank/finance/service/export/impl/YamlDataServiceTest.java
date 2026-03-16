package org.tigerbank.finance.service.export.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;
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

class YamlDataServiceTest {

    @TempDir
    Path tempDir;

    private IBankAccountRepository accountRepo;
    private ICategoryRepository categoryRepo;
    private IOperationRepository operationRepo;
    private YamlDataService service;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        operationRepo = mock(IOperationRepository.class);
        service = new YamlDataService(accountRepo, categoryRepo, operationRepo);
    }

    @Test
    void exportToFile() throws IOException {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("1000.00"));

        when(accountRepo.findAll()).thenReturn(List.of(account));
        when(categoryRepo.findAll()).thenReturn(List.of());
        when(operationRepo.findAll()).thenReturn(List.of());

        String filePath = tempDir.toString() + "/";

        assertDoesNotThrow(() -> service.exportToFile(filePath));

        File exportedFile = new File(filePath + "full-data.yaml");
        assertTrue(exportedFile.exists());
    }

    @Test
    void exportWithCategories() throws IOException {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("500.00"));
        Category category = Category.create("Продукты", CategoryType.EXPENSE);

        when(accountRepo.findAll()).thenReturn(List.of(account));
        when(categoryRepo.findAll()).thenReturn(List.of(category));
        when(operationRepo.findAll()).thenReturn(List.of());

        String filePath = tempDir.toString() + "/";

        assertDoesNotThrow(() -> service.exportToFile(filePath));

        File exportedFile = new File(filePath + "full-data.yaml");
        assertTrue(exportedFile.exists());
    }
}