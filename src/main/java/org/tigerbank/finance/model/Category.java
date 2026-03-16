package org.tigerbank.finance.model;


import java.util.UUID;

public final class Category {
    private UUID id;
    private CategoryType type;
    private String name;

    private Category() {}

    public static Category create(String name, CategoryType type) {
        if (type == null) {
            throw new IllegalArgumentException("Тип категории не может быть null");
        }
        Category category = new Category();
        category.id = UUID.randomUUID();
        category.name= name;
        category.type = type;

        return category;
    }


    // ======= ГЕТТЕРЫ ======
    public UUID getId() {
        return id;
    }

    public CategoryType getType() {
        return  type;
    }

    public String getName() {
        return name;
    }


    // ======== БИЗНЕС МЕТОДЫ ===========

    public void rename(String newName) {
        this.name = validateName(newName);
    }

    /**
     * Проверяет, может ли операция данного типа быть привязана к этой категории
     * @param operationType тип операции
     * @throws IllegalArgumentException если типы не совместимы
     */
    public void validateOperationCompatibility(OperationType operationType) {
        type.validateOperationType(operationType);
    }

    /**
     * Проверяет допустимость введенного имени, а также удаляет пробелы в конце слова
     * @param name потенциальное имя
     * @return удаленная строка без пробелов, если строка прошла проверки
     * @throws IllegalArgumentException если строка не удовлетворяет заданным критериям
     */
    private String validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Название категории не может быть null");
        }

        String t = name.trim();
        if (t.isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        if (t.length() > 50) {
            throw new IllegalArgumentException("Название категории не может превышать 50 символов");
        }

        return t;
    }

    // ======== СЛУЖЕБНЫЕ МЕТОДЫ ===========
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", type=" + type + ", name=" + name +"}";
    }








}
