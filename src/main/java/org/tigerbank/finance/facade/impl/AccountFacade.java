package org.tigerbank.finance.facade.impl;

import org.springframework.stereotype.Component;
import org.tigerbank.finance.facade.IAccountFacade;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.service.IOperationService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class AccountFacade implements IAccountFacade {
    private final IBankAccountRepository accountRepo;
    private final IOperationService operationService;

    public AccountFacade(IBankAccountRepository accountRepo,
                         IOperationService operationService) {
        this.accountRepo = accountRepo;
        this.operationService = operationService;
    }

    @Override
    public BankAccount createAccount(BankAccount account) {
        BankAccount savedAccount = accountRepo.save(account);
        operationService.addOperation(Operation.create(
                OperationType.INCOME,
                savedAccount.getId(),
                savedAccount.getBalance(),
                "Первое пополнение",
                null
        ));
        return accountRepo.findById(savedAccount.getId()).get();
    }

    @Override
    public void closeAccount(UUID id) {
        Optional<BankAccount> account = accountRepo.findById(id);

        if (account.isEmpty()) {
            System.out.println("Невозможно закрыть счет: нет аккаунта с таким номером!");
            return;
        }
        // если баланс больше нуля - нельзя закрыть счёт
        if (account.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
            System.out.println("Невозможно закрыть счет: баланс больше нуля.");
            return;
        }
        // удаляем счет из хранилища
        accountRepo.deleteById(id);
    }

    @Override
    public List<BankAccount> getAllAccounts() {
        return accountRepo.findAll();
    }

}