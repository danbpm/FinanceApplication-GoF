package org.tigerbank.finance.service.impl;

import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.IOperationRepository;

import org.springframework.stereotype.Service;
import org.tigerbank.finance.service.IBalanceRecalculator;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class BalanceRecalculator implements IBalanceRecalculator {
    private final IBankAccountRepository accountRepo;
    private final IOperationRepository operationRepo;

    public BalanceRecalculator(IBankAccountRepository accountRepo,
                               IOperationRepository operationRepo) {
        this.accountRepo = accountRepo;
        this.operationRepo = operationRepo;
    }

    @Override
    public void recalculateBalance(UUID accountId) {
        BankAccount account = accountRepo.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Счет не найден: " + accountId));

        BigDecimal newBalance = operationRepo.findByAccountId(accountId)
                .stream()
                .map(Operation::getAmountWithSign)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        account.setBalanceUnsafe(newBalance);
        accountRepo.save(account);
    }
}
