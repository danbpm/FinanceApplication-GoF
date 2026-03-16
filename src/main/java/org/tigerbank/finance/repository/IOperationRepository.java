package org.tigerbank.finance.repository;

import org.tigerbank.finance.model.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOperationRepository {
    Operation save(Operation operation);
    Optional<Operation> findById(UUID id);
    List<Operation> findAll();
    void deleteById(UUID id);
    List<Operation> findByAccountId(UUID accountId);
    List<Operation> findByPeriod(LocalDate from, LocalDate to);
    List<Operation> findByAccountAndPeriod(UUID accountId, LocalDate from, LocalDate to);
}
