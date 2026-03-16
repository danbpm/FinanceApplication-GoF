package org.tigerbank.finance.facade.impl;

import org.springframework.stereotype.Component;
import org.tigerbank.finance.facade.IAnalyticsFacade;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.service.IAnalyticsService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Component
public class AnalyticsFacade implements IAnalyticsFacade {
    private final IAnalyticsService analyticsService;

    public AnalyticsFacade(IAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @Override
    public void showExpansesByCategory(OperationType type, LocalDate from, LocalDate to) {
        Map<Category, BigDecimal> expenses = analyticsService.groupByCategory(from, to, type);
        expenses.forEach((cat, amt) ->
                System.out.printf("   • %-15s: %,.2f у.е.%n", cat.getName(), amt)
        );
    }

    @Override
    public void showExpansesByCategory(UUID accountId, OperationType type, LocalDate from, LocalDate to) {
        Map<Category, BigDecimal> expenses = analyticsService.groupByCategory(accountId, from ,to, type);
        expenses.forEach((cat, amt) ->
                System.out.printf("   • %-15s: %,.2f у.е.%n", cat.getName(), amt)
        );
    }

    @Override
    public void showStatisticsByAccount(UUID accountId, LocalDate from, LocalDate to) {
        System.out.println(analyticsService.getStatisticsByAccount(accountId, from, to));
    }

    @Override
    public void saveStatisticsByAccount(String filePath,
                                        UUID accountId,
                                        LocalDate from,
                                        LocalDate to) {
        try {
            String exportData = analyticsService.getStatisticsByAccount(accountId, from, to);
            Files.writeString(Path.of(filePath), exportData);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось записать в файл: " + e.getMessage());
        }
    }

}
