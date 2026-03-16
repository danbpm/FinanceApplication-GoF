package org.tigerbank.finance.service;

import org.tigerbank.finance.model.Operation;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IOperationService {
     Operation addOperation(Operation operation);
     void deleteOperation(UUID operationId);
     List<Operation> getAccountOperations(UUID accountId);
     BigDecimal getAccountBalance(UUID accountId);
}
