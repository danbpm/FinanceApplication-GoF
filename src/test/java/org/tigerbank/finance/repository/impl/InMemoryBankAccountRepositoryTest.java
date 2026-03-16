package org.tigerbank.finance.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBankAccountRepositoryTest {

    private InMemoryBankAccountRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryBankAccountRepository();
    }

    @Test
    void saveAccount() {
        BankAccount account = BankAccount.create("Тестовый счёт", new BigDecimal("1000.00"));

        BankAccount saved = repository.save(account);

        assertNotNull(saved);
        assertEquals(account.getId(), saved.getId());
    }

    @Test
    void findByIdExisting() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("100.00"));
        repository.save(account);

        Optional<BankAccount> found = repository.findById(account.getId());

        assertTrue(found.isPresent());
        assertEquals(account.getId(), found.get().getId());
    }

    @Test
    void findByIdNotExisting() {
        Optional<BankAccount> found = repository.findById(UUID.randomUUID());

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll() {
        repository.save(BankAccount.create("Счёт 1", new BigDecimal("100.00")));
        repository.save(BankAccount.create("Счёт 2", new BigDecimal("200.00")));

        List<BankAccount> accounts = repository.findAll();

        assertEquals(2, accounts.size());
    }

    @Test
    void deleteById() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("500.00"));
        repository.save(account);

        repository.deleteById(account.getId());

        Optional<BankAccount> found = repository.findById(account.getId());
        assertTrue(found.isEmpty());
    }
}