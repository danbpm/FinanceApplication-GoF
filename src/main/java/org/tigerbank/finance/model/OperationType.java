package org.tigerbank.finance.model;

public enum OperationType {
    INCOME("Доход"),
    EXPENSE("Расход");

    private final String name;

    OperationType(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    /**
     * Возвращает множитель в зависимости от типа операции для расчёта баланса.
     * @return INCOME -> +1, EXPENSE -> -1
     */
    public int getBalanceMultiplier() {
        return this == INCOME ? 1 : -1;
    }
}
