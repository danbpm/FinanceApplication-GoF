package org.tigerbank.finance.service;

import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface IAnalyticsService {
    /**
     * Расчет чистой прибыли за период
     * @param from От
     * @param to До
     * @return Чистая прибыль
     */
    BigDecimal calculateNetIncome(LocalDate from, LocalDate to);

    BigDecimal calculateNetIncomeByAccount(UUID id, LocalDate from, LocalDate to);

    /**
     * Группировка по категориям сумм выбранного типа операций(доход/расход) за указанный период
     * @param from От
     * @param to До
     * @param type Тип операции
     * @return Возвращает отображение категория -> сумма по операциям
     */
    Map<Category, BigDecimal> groupByCategory(LocalDate from, LocalDate to, OperationType type);

    Map<Category, BigDecimal> groupByCategory(UUID accountId, LocalDate from, LocalDate to, OperationType type);

    String getStatisticsByAccount(UUID accountId, LocalDate from, LocalDate to);
}
