package org.tigerbank.finance.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public final class Operation {
    private UUID id;
    private OperationType type;
    private UUID bankAccountId;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private UUID categoryId;

    private Operation() {}

    public static Operation create (OperationType type,
                                    UUID accountId,
                                    BigDecimal amount,
                                    String description,
                                    UUID categoryId) {

        if (type == null) {
            throw new IllegalArgumentException("Тип операции обязателен");
        }
        if (accountId == null) {
            throw new IllegalArgumentException("ID счета обязателен");
        }

        Operation op = new Operation();
        op.id = UUID.randomUUID();
        op.type = type;
        op.bankAccountId = accountId;
        op.amount = validateAmount(amount);
        op.description = validateDescription(description);
        op.date = LocalDate.now();
        op.categoryId = categoryId;

        return op;
    }

    //  ===== СЕТТЕРЫ =====
    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    //  ===== ГЕТТЕРЫ =====
    public UUID getId() {
       return id;
    }

    public OperationType getType() {
        return type;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public UUID getCategoryId() {
        return categoryId;
    }


    // ====== БИЗНЕС МЕТОДЫ =======

    /**
     * Обновление описания операции
     * @param newDescriprion новое описание
     */
    public void updateDescription(String newDescriprion) {
        this.description = validateDescription(newDescriprion);
    }

    /**
     * Привязывает категорию к операции с проверкой совместимости типов
     * @param category категория, которую мы привязываем к операции
     */
    public void assignCategory(Category category) {
        if (category == null) {
            this.categoryId = null;
            return;
        }
        // проверяем совместимость категории и операции
        category.validateOperationCompatibility(this.type);
        this.categoryId = category.getId();
    }

    /**
     * Отвязывает категорию от операции
     */
    public void removeCategory() {
        this.categoryId = null;
    }

    /**
     * Возвращает сумму с учётом типа операции для расчёта баланса
     */
    @JsonIgnore
    public BigDecimal getAmountWithSign() {
        return amount.multiply(BigDecimal.valueOf(type.getBalanceMultiplier()));
    }

    private static BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Сумма операций не может быть null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Сумма операции должна быть положительной: " + amount);
        }
        return amount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Проверяет валидность указанной даты (дата не должна быть будущим)
     */
    private static LocalDate validateDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Дата операции не может быть в будущем: " + date);
        }

        return date;
    }

    /**
     * Проверяет введенное описание (не должно быть пустым и превышать некоторый заданный предел)
     */
    private static String validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            return null;
        }
        String t = description.trim();
        if (t.length() > 255) {
            throw new IllegalArgumentException("Описание не может превышать 255 символов");
        }
        return t;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", type=" + type.getName() +
                ", account=" + bankAccountId +
                ", amount=" + amount + " у.e." +
                ", date=" + date +
                (description != null ? ", desc=" + description : "") +
                (categoryId != null ? ", category=" + categoryId : "") +
                "}";
    }
}
