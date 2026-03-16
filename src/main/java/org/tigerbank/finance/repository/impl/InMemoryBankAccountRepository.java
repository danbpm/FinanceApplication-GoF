package org.tigerbank.finance.repository.impl;

import org.tigerbank.finance.model.BankAccount;

import org.springframework.stereotype.Repository;
import org.tigerbank.finance.repository.IBankAccountRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryBankAccountRepository implements IBankAccountRepository {
    private final Map<UUID, BankAccount> storage = new ConcurrentHashMap<>();

    @Override
    public BankAccount save(BankAccount account) {
        storage.put(account.getId(), account);
        return account;
    }

    @Override
    public Optional<BankAccount> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BankAccount> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
