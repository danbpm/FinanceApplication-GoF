package org.tigerbank.finance.service;

import java.util.UUID;

public interface IBalanceRecalculator {
    void recalculateBalance(UUID accountId);
}
