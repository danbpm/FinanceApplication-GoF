package org.tigerbank.finance.facade;

import org.tigerbank.finance.model.BankAccount;

import java.util.List;
import java.util.UUID;

public interface IAccountFacade {
    public BankAccount createAccount(BankAccount account);
    public void closeAccount(UUID id);
    public List<BankAccount> getAllAccounts();
}
