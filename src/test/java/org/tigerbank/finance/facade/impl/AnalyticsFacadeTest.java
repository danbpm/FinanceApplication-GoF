package org.tigerbank.finance.facade.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.service.IAnalyticsService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyticsFacadeTest {

    private IAnalyticsService analyticsService;
    private AnalyticsFacade facade;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        analyticsService = mock(IAnalyticsService.class);
        facade = new AnalyticsFacade(analyticsService);
    }

    @Test
    void showExpansesByCategory() {
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();
        Category category = Category.create("Продукты", CategoryType.EXPENSE);

        when(analyticsService.groupByCategory(from, to, OperationType.EXPENSE))
                .thenReturn(Map.of(category, new BigDecimal("1500.00")));

        assertDoesNotThrow(() ->
                facade.showExpansesByCategory(OperationType.EXPENSE, from, to)
        );

        verify(analyticsService).groupByCategory(from, to, OperationType.EXPENSE);
    }

    @Test
    void showExpansesByCategoryWithAccount() {
        UUID accountId = UUID.randomUUID();
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();
        Category category = Category.create("Продукты", CategoryType.EXPENSE);

        when(analyticsService.groupByCategory(accountId, from, to, OperationType.EXPENSE))
                .thenReturn(Map.of(category, new BigDecimal("1500.00")));

        assertDoesNotThrow(() ->
                facade.showExpansesByCategory(accountId, OperationType.EXPENSE, from, to)
        );

        verify(analyticsService).groupByCategory(accountId, from, to, OperationType.EXPENSE);
    }

    @Test
    void showStatisticsByAccount() {
        UUID accountId = UUID.randomUUID();
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        when(analyticsService.getStatisticsByAccount(accountId, from, to))
                .thenReturn("{\"account\": \"Тест\", \"balance\": 1000.00}");

        assertDoesNotThrow(() ->
                facade.showStatisticsByAccount(accountId, from, to)
        );

        verify(analyticsService).getStatisticsByAccount(accountId, from, to);
    }

    @Test
    void saveStatisticsByAccount() throws IOException {
        UUID accountId = UUID.randomUUID();
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();
        Path filePath = tempDir.resolve("statistics.json");

        when(analyticsService.getStatisticsByAccount(accountId, from, to))
                .thenReturn("{\"account\": \"Тест\", \"balance\": 1000.00}");

        assertDoesNotThrow(() ->
                facade.saveStatisticsByAccount(filePath.toString(), accountId, from, to)
        );

        assertTrue(Files.exists(filePath));
        verify(analyticsService).getStatisticsByAccount(accountId, from, to);
    }

    @Test
    void saveStatisticsByAccountInvalidPath() {
        UUID accountId = UUID.randomUUID();
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate to = LocalDate.now();

        when(analyticsService.getStatisticsByAccount(accountId, from, to))
                .thenReturn("{\"account\": \"Тест\"}");

        assertThrows(RuntimeException.class, () ->
                facade.saveStatisticsByAccount("/nonexistent/path/file.json", accountId, from, to)
        );
    }
}