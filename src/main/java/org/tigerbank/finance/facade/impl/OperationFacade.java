package org.tigerbank.finance.facade.impl;


import org.tigerbank.finance.facade.IOperationFacade;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.repository.IOperationRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.service.IBalanceRecalculator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OperationFacade implements IOperationFacade {

    private final IOperationRepository operationRepo;
    private final ICategoryRepository categoryRepo;
    private final IBankAccountRepository accountRepo;
    private final IBalanceRecalculator recalculator;

    public OperationFacade(IOperationRepository operationRepo,
                           ICategoryRepository categoryRepo,
                       IBankAccountRepository accountRepo,
                           IBalanceRecalculator recalculator) {
        this.operationRepo = operationRepo;
        this.categoryRepo = categoryRepo;
        this.accountRepo = accountRepo;
        this.recalculator = recalculator;
    }


    @Override
    public Operation createOperation(Operation operation) {
        if (accountRepo.findById(operation.getBankAccountId()).isEmpty()) {
            throw new IllegalArgumentException("Счёт не найден");
        }
        if (operation.getCategoryId() != null) {
            categoryRepo.findById(operation.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
    }

        Operation saved = operationRepo.save(operation);

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
    public Optional<Operation> getOperation(UUID id) {
        return operationRepo.findById(id);
    }

    @Override
    public List<Operation> getAllOperations() {
        return operationRepo.findAll();
    }

    @Override
    public List<Operation> getByAccount(UUID accountId) {
        return operationRepo.findByAccountId(accountId);
    }

    @Override
    public List<Operation> getByPeriod(LocalDate from, LocalDate to) {
        return operationRepo.findByPeriod(from, to);
    }
}