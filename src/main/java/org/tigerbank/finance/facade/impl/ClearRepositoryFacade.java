package org.tigerbank.finance.facade.impl;

import org.springframework.stereotype.Component;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;

import java.util.List;

@Component
public class ClearRepositoryFacade  {
    private final IBankAccountRepository accountRepository;
    private final ICategoryRepository categoryRepository;
    private final IOperationRepository operationRepository;
    public ClearRepositoryFacade(IBankAccountRepository accountRepository,
                                 ICategoryRepository categoryRepository,
                                 IOperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public void clearAll() {
        List<Operation> operations = operationRepository.findAll();
        List<BankAccount> accounts = accountRepository.findAll();
        List<Category> categories = categoryRepository.findAll();

        for (Operation op : operations) {
            operationRepository.deleteById(op.getId());
        }
        for (BankAccount account : accounts) {
            accountRepository.deleteById(account.getId());
        }
        for (Category cat : categories) {
            categoryRepository.deleteById(cat.getId());
        }
    }
}
