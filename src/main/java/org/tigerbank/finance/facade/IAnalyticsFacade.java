package org.tigerbank.finance.facade;

import org.tigerbank.finance.model.OperationType;

import java.time.LocalDate;
import java.util.UUID;

public interface IAnalyticsFacade {
    public void showExpansesByCategory(OperationType type,
                                       LocalDate from,
                                       LocalDate to);

    public void showExpansesByCategory(UUID accountId,
                                       OperationType type,
                                       LocalDate from,
                                       LocalDate to);

    public void showStatisticsByAccount(UUID accountId,
                                        LocalDate from,
                                        LocalDate to);


    public void saveStatisticsByAccount(String filePath,
                                        UUID accountId,
                                        LocalDate from,
                                        LocalDate to);

}
