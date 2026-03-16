package org.tigerbank.finance.service.export;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.CategoryType;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;
import org.tigerbank.finance.repository.IBankAccountRepository;
import org.tigerbank.finance.repository.ICategoryRepository;
import org.tigerbank.finance.repository.IOperationRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class DataServiceTest {

    private IBankAccountRepository accountRepo;
    private ICategoryRepository categoryRepo;
    private IOperationRepository operationRepo;
    private TestDataService service;

    @BeforeEach
    void setUp() {
        accountRepo = mock(IBankAccountRepository.class);
        categoryRepo = mock(ICategoryRepository.class);
        operationRepo = mock(IOperationRepository.class);
        service = new TestDataService(accountRepo, categoryRepo, operationRepo);
    }

    @Test
    void clearRepositories() {
        UUID accountId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();
        UUID operationId = UUID.randomUUID();

        BankAccount account = BankAccount.create("Счёт", new BigDecimal("100.00"));
        Category category = Category.create("Категория", CategoryType.INCOME);
        Operation operation = Operation.create(OperationType.INCOME, accountId,
                new BigDecimal("50.00"), "Описание", categoryId);

        when(accountRepo.findAll()).thenReturn(List.of(account));
        when(categoryRepo.findAll()).thenReturn(List.of(category));
        when(operationRepo.findAll()).thenReturn(List.of(operation));

        service.clearRepositories();

        verify(accountRepo).deleteById(account.getId());
        verify(categoryRepo).deleteById(category.getId());
        verify(operationRepo).deleteById(operation.getId());
    }

    @Test
    void saveDataToRepositories() {
        BankAccount account = BankAccount.create("Счёт", new BigDecimal("100.00"));
        Category category = Category.create("Категория", CategoryType.INCOME);
        Operation operation = Operation.create(OperationType.INCOME, account.getId(),
                new BigDecimal("50.00"), "Описание", category.getId());

        service.saveDataToRepositories(
                new org.tigerbank.finance.dto.FinanceData(
                        List.of(account),
                        List.of(category),
                        List.of(operation)
                )
        );

        verify(accountRepo).save(account);
        verify(categoryRepo).save(category);
        verify(operationRepo).save(operation);
    }

    // Тестовая реализация для проверки абстрактных методов
    static class TestDataService extends DataService {
        public TestDataService(IBankAccountRepository accountRepo,
                               ICategoryRepository categoryRepo,
                               IOperationRepository operationRepo) {
            super(accountRepo, categoryRepo, operationRepo);
        }

        @Override
        public void exportToFile(String filePath) {
            // Не реализуем для теста
        }

        @Override
        protected org.tigerbank.finance.dto.FinanceData parseFromFile(String filePath) {
            return null;
        }
    }
}