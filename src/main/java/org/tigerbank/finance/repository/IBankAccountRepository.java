package org.tigerbank.finance.repository;

import org.tigerbank.finance.model.BankAccount;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IBankAccountRepository {
    BankAccount save(BankAccount account);
    Optional<BankAccount> findById (UUID id);
    List<BankAccount> findAll();
    void deleteById(UUID id);
}
