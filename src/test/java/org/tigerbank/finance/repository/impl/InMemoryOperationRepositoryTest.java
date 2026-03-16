package org.tigerbank.finance.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tigerbank.finance.model.Operation;
import org.tigerbank.finance.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryOperationRepositoryTest {

    private InMemoryOperationRepository repository;
    private UUID testAccountId;

    @BeforeEach
    void setUp() {
        repository = new InMemoryOperationRepository();
        testAccountId = UUID.randomUUID();
    }

    @Test
    void saveOperation() {
        Operation operation = createTestOperation();

        Operation saved = repository.save(operation);

        assertNotNull(saved);
        assertEquals(operation.getId(), saved.getId());
    }

    @Test
    void findByIdExisting() {
        Operation operation = createTestOperation();
        repository.save(operation);

        Optional<Operation> found = repository.findById(operation.getId());

        assertTrue(found.isPresent());
    }

    @Test
    void findByIdNotExisting() {
        Optional<Operation> found = repository.findById(UUID.randomUUID());

        assertTrue(found.isEmpty());
    }

    @Test
    void findAll() {
        repository.save(createTestOperation());
        repository.save(createTestOperation());

        List<Operation> operations = repository.findAll();

        assertEquals(2, operations.size());
    }

    @Test
    void deleteById() {
        Operation operation = createTestOperation();
        repository.save(operation);

        repository.deleteById(operation.getId());

        Optional<Operation> found = repository.findById(operation.getId());
        assertTrue(found.isEmpty());
    }

    @Test
    void findByAccountId() {
        UUID accountId = UUID.randomUUID();
        Operation op1 = Operation.create(OperationType.INCOME, accountId, new BigDecimal("100.00"), "Описание 1", null);
        Operation op2 = Operation.create(OperationType.INCOME, UUID.randomUUID(), new BigDecimal("200.00"), "Описание 2", null);

        repository.save(op1);
        repository.save(op2);

        List<Operation> found = repository.findByAccountId(accountId);

        assertEquals(1, found.size());
    }

    @Test
    void findByPeriod() {
        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);

        Operation operation = createTestOperation();
        repository.save(operation);

        List<Operation> found = repository.findByPeriod(from, to);

        assertEquals(1, found.size());
    }

    private Operation createTestOperation() {
        return Operation.create(
                OperationType.INCOME,
                testAccountId,
                new BigDecimal("100.00"),
                "Тестовая операция",
                null
        );
    }
}