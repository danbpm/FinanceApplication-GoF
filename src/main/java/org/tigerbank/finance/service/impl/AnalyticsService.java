package org.tigerbank.finance.service.impl;

import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.model.Category;

import org.springframework.stereotype.Service;
import org.tigerbank.finance.service.IAnalyticsService;
import org.tigerbank.finance.service.IOperationService;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnalyticsService implements IAnalyticsService {
    private final IOperationRepository operationRepo;
    private final IBankAccountRepository accountRepo;
    private final ICategoryRepository categoryRepo;

    public AnalyticsService(IOperationRepository operationRepo,
                            ICategoryRepository categoryRepo,
                            IBankAccountRepository accountRepo) {
        this.operationRepo = operationRepo;
        this.categoryRepo = categoryRepo;
        this.accountRepo = accountRepo;
    }

    @Override
    public BigDecimal calculateNetIncome(LocalDate from, LocalDate to) {
        List<Operation> ops = operationRepo.findByPeriod(from, to);
        BigDecimal income = ops.stream()
                .filter((op) -> op.getType() == OperationType.INCOME)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal expense = ops.stream()
                .filter((op) -> op.getType() == OperationType.EXPENSE)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return income.subtract(expense);
    }

    @Override
    public BigDecimal calculateNetIncomeByAccount(UUID accountId, LocalDate from, LocalDate to) {
        List<Operation> ops = operationRepo.findByAccountAndPeriod(accountId, from, to);
        BigDecimal income = ops.stream()
                .filter((op) -> op.getType() == OperationType.INCOME)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal expense = ops.stream()
                .filter((op) -> op.getType() == OperationType.EXPENSE)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return income.subtract(expense);
    }


    /**
     * @implNote Неизвестные категории(те у которых ID равен null) не попадают в результирующую выборку
     */
    @Override
    public Map<Category, BigDecimal> groupByCategory(LocalDate from, LocalDate to, OperationType opType) {
        return operationRepo.findByPeriod(from, to).stream()
              .filter((op) -> op.getType() == opType && op.getCategoryId() != null)
              .collect(Collectors.groupingBy(
                      (op) -> categoryRepo.findById(op.getCategoryId())
                              .orElseThrow(() -> new IllegalArgumentException("Категория отсутствует")),
                      Collectors.reducing(BigDecimal.ZERO, Operation::getAmount, BigDecimal::add)
              ));
    }

    @Override
    public Map<Category, BigDecimal> groupByCategory(UUID accountId, LocalDate from, LocalDate to, OperationType opType) {
        return operationRepo.findByAccountAndPeriod(accountId, from, to).stream()
                .filter((op) -> op.getType() == opType && op.getCategoryId() != null)
                .collect(Collectors.groupingBy(
                        (op) -> categoryRepo.findById(op.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Категория отсутствует")),
                        Collectors.reducing(BigDecimal.ZERO, Operation::getAmount, BigDecimal::add)
                ));
    }
    @Override
    public String getStatisticsByAccount(UUID accountId, LocalDate from, LocalDate to) {

        Optional<BankAccount> accountOpt = accountRepo.findById(accountId);
        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Ошибка расчета статистики: нет счета с таким номером");
        }
        BankAccount account = accountOpt.get();

        String exportContent = """
            {
              "account": "%s",
              "balance": %.2f,
              "total_operations": %d,
              "income_operations": %d,
              "expense_operations": %d,
              "period_start": "%s",
              "period_end": "%s",
              "net_income": %.2f
            }
            """.formatted(
                account.getName(),
                account.getBalance(),
                operationRepo.findByAccountId(accountId).size(),
                operationRepo.findByAccountId(accountId).stream()
                        .filter(op -> op.getType() == OperationType.INCOME).count(),
                operationRepo.findByAccountId(accountId).stream()
                        .filter(op -> op.getType() == OperationType.EXPENSE).count(),
                from,
                to,
                calculateNetIncomeByAccount(accountId, from, to)
        );

        return exportContent;
    }

}
