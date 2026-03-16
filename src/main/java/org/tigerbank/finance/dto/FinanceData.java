package org.tigerbank.finance.dto;

import org.tigerbank.finance.model.BankAccount;
import org.tigerbank.finance.model.Category;
import org.tigerbank.finance.model.Operation;

import java.util.List;

/**
 * Контейнер для экспорта/импорта всех финансовых данных.
 */
public class FinanceData {
    public List<BankAccount> accounts;
    public List<Category> categories;
    public List<Operation> operations;

    public FinanceData() {}

    public FinanceData(List<BankAccount> accounts, List<Category> categories, List<Operation> operations) {
        this.accounts = accounts;
        this.categories = categories;
        this.operations = operations;
    }
}