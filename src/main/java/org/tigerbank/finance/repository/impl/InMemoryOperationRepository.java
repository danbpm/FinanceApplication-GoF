package org.tigerbank.finance.repository.impl;

import org.tigerbank.finance.model.Operation;

import org.springframework.stereotype.Repository;
import org.tigerbank.finance.repository.IOperationRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Repository
public class InMemoryOperationRepository implements IOperationRepository {
    private Map<UUID, Operation> storage = new ConcurrentHashMap<>();

    @Override
    public Operation save(Operation operation) {
        storage.put(operation.getId(), operation);
        return operation;
    }

    @Override
    public Optional<Operation> findById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("operationId не может быть null");
        }
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Operation> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("operationId не может быть null");
        }
        storage.remove(id);
    }

    @Override
    public List<Operation> findByAccountId(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("accountId не может быть null");
        }

        return storage.values()
                .stream()
                .filter((op) -> accountId.equals(op.getBankAccountId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByPeriod(LocalDate from, LocalDate to) {
        return storage.values()
                .stream()
                .filter((op) ->  (!op.getDate().isBefore(from) && !op.getDate().isAfter(to)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Operation> findByAccountAndPeriod(UUID accountId, LocalDate from, LocalDate to) {
        return storage.values()
                .stream()
                .filter((op) ->  (accountId.equals(op.getBankAccountId()) && !op.getDate().isBefore(from) && !op.getDate().isAfter(to)))
                .collect(Collectors.toList());
    }
}
