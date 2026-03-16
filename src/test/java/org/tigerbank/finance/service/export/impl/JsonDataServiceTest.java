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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonDataServiceTest {

    @TempDir
    Path tempDir;

    private IBankAccountRepository accountRepo;
    private ICategoryRepository categoryRepo;
    private IOperationRepository operationRepo;
    private JsonDataService service;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        operationRepo = mock(IOperationRepository.class);
        service = new JsonDataService(accountRepo, categoryRepo, operationRepo);
    }

    @Test
    void exportToFile() throws IOException {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Тестовый счёт", new BigDecimal("1000.00"));

        when(accountRepo.findAll()).thenReturn(List.of(account));
        when(categoryRepo.findAll()).thenReturn(List.of());
        when(operationRepo.findAll()).thenReturn(List.of());

        String filePath = tempDir.toString() + "/";

        assertDoesNotThrow(() -> service.exportToFile(filePath));

        File exportedFile = new File(filePath + "full-data.json");
        assertTrue(exportedFile.exists());
    }

    @Test
    void exportAndImport() throws IOException {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("500.00"));
        Category category = Category.create("Зарплата", CategoryType.INCOME);

        when(accountRepo.findAll()).thenReturn(List.of(account));
        when(categoryRepo.findAll()).thenReturn(List.of(category));
        when(operationRepo.findAll()).thenReturn(List.of());

        String filePath = tempDir.toString() + "/";

        // Экспорт
        service.exportToFile(filePath);

        // Очистка репозиториев (эмулируем)
        doNothing().when(accountRepo).deleteById(any());
        doNothing().when(categoryRepo).deleteById(any());
        doNothing().when(operationRepo).deleteById(any());
        when(accountRepo.findAll()).thenReturn(List.of());
        when(categoryRepo.findAll()).thenReturn(List.of());

        // Импорт (не должен выбрасывать)
        assertDoesNotThrow(() -> service.importFromFile(filePath));
    }
}