package org.tigerbank.finance.service.impl;

import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IBankAccountRepository;

import org.springframework.stereotype.Service;
import org.tigerbank.finance.service.IBalanceRecalculator;
import org.tigerbank.finance.service.IOperationService;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.List;

@Service
public class OperationService implements IOperationService {
    private final IBankAccountRepository accountRepo;
    private final IOperationRepository operationRepo;
    private final ICategoryRepository categoryRepo;
    private final IBalanceRecalculator recalculator;

    public OperationService(IBankAccountRepository accountRepo,
                            IOperationRepository operationRepo,
                            ICategoryRepository categoryRepo,
                            IBalanceRecalculator recalculator) {
        this.accountRepo = accountRepo;
        this.operationRepo = operationRepo;
        this.categoryRepo = categoryRepo;
        this.recalculator = recalculator;
    }

    @Override
    public Operation addOperation(Operation operation) {
        if (accountRepo.findById(operation.getBankAccountId()).isEmpty()) {
            throw new IllegalArgumentException("Счёт не найден: " + operation.getBankAccountId());
        }
        // необходимо обеспечить непротиворечивость данных, поэтому проверяем
        // соответствует ли приведенная категория какой-либо из репозитория
        if (operation.getCategoryId() != null) {
           Category cat = categoryRepo.findById(operation.getCategoryId())
                   .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
           // проверим совместимость категории и операции
           cat.validateOperationCompatibility(operation.getType());
        }

        Operation saved = operationRepo.save(operation);
        // пересчитаем баланс у аккаунта, кому принадлежит эта операция
        recalculator.recalculateBalance(operation.getBankAccountId());
        return saved;
    }

    @Override
    public void deleteOperation(UUID id) {
        Operation op = operationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Операция не найдена"));
        operationRepo.deleteById(id);
        recalculator.recalculateBalance(op.getBankAccountId());
    }

    @Override
    public List<Operation> getAccountOperations(UUID accountId) {
        return  operationRepo.findByAccountId(accountId);
    }

    @Override
    public BigDecimal getAccountBalance(UUID id) {
        return accountRepo.findById(id)
                .map(BankAccount::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("Счет не найден"));
    }
}

