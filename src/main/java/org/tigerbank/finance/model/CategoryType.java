package org.tigerbank.finance.model;

public enum CategoryType {
    INCOME("Доход"),
    EXPENSE("Расход");

    private final String name;

    CategoryType(String name) {
        this.name = name;
    }

    public String genName() {
        return name;
    }

    public void validateOperationType(OperationType operationType) {
        boolean isValid = (this == INCOME && operationType == OperationType.INCOME) ||
                (this == EXPENSE && operationType == OperationType.EXPENSE);

        if (!isValid) {
            throw new IllegalArgumentException(
                    String.format("Нельзя привязать операцию типа '%s' к категории  типа '%s'",
                            operationType.getName(), this.genName()));
        }
    }
}
