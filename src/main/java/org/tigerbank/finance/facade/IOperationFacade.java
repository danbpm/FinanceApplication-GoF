package org.tigerbank.finance.facade;

import org.tigerbank.finance.model.Operation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IOperationFacade {
    public Operation createOperation(Operation operation);
    public void deleteOperation(UUID id);
    public Optional<Operation> getOperation(UUID id);
    public List<Operation> getAllOperations();
    public List<Operation> getByAccount(UUID accountId);
    public List<Operation> getByPeriod(LocalDate from, LocalDate to);
}
