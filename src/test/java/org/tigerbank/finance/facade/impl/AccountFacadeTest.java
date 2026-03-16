package org.tigerbank.finance.facade.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.service.IOperationService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountFacadeTest {

    private IBankAccountRepository accountRepo;
    private IOperationService operationService;
    private AccountFacade facade;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        operationService = mock(IOperationService.class);
        facade = new AccountFacade(accountRepo, operationService);
    }

    @Test
    void createAccount() {
        BankAccount account = BankAccount.create("Тестовый счёт", new BigDecimal("1000.00"));

        when(accountRepo.save(any())).thenReturn(account);
        when(accountRepo.findById(any())).thenReturn(Optional.of(account));

        BankAccount result = facade.createAccount(account);

        assertNotNull(result);
        verify(accountRepo).save(account);
        verify(operationService).addOperation(any(Operation.class));
    }

    @Test
    void closeAccountWithZeroBalance() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", BigDecimal.ZERO);

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));

        facade.closeAccount(accountId);

        verify(accountRepo).deleteById(accountId);
    }

    @Test
    void closeAccountWithNonZeroBalance() {
        UUID accountId = UUID.randomUUID();
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("500.00"));

        when(accountRepo.findById(accountId)).thenReturn(Optional.of(account));

        facade.closeAccount(accountId);

        verify(accountRepo, never()).deleteById(accountId);
    }

    @Test
    void closeAccountNotFound() {
        UUID accountId = UUID.randomUUID();

        when(accountRepo.findById(accountId)).thenReturn(Optional.empty());

        facade.closeAccount(accountId);

        verify(accountRepo, never()).deleteById(accountId);
    }

    @Test
    void getAllAccounts() {
        when(accountRepo.findAll()).thenReturn(List.of(
                BankAccount.create("Счёт 1", new BigDecimal("100.00")),
                BankAccount.create("Счёт 2", new BigDecimal("200.00"))
        ));

        List<BankAccount> accounts = facade.getAllAccounts();

        assertEquals(2, accounts.size());
    }
}